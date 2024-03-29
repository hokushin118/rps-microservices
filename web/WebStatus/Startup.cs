﻿using HealthChecks.UI.Client;
using HealthChecks.UIAndApi.Options;
using WebStatus.Extensions;

namespace WebStatus;

public class Startup
{
    private const string RpsCmdServicePath = "rps-cmd-api";
    private const string RpsQryServicePath = "rps-qry-api";
    private const string ScoreCmdServicePath = "score-cmd-api";
    private const string ScoreQryServicePath = "score-qry-api";
    private const string HealthPath = "health";
    private const string ActuatorPath = "actuator";
    private const string DockerBaseUrl = "http://host.docker.internal";

    public IEnumerable<string> ApiTags { get; } = new[] { "api", "rest", "grpc" };

    public IConfiguration Configuration { get; }

    public Startup(IConfiguration configuration)
    {
        Configuration = configuration;
    }

    public void ConfigureServices(IServiceCollection services)
    {
        RegisterAppInsights(services);

        services.Configure<RemoteOptions>(options => Configuration.Bind(options));

        services.AddMvc();

        services.AddOptions();
        services
            .AddHealthChecks()
            .AddCheck("self", () => HealthCheckResult.Healthy())
            .AddApiHealth("RPS Game Command Microservice Health Check", ApiTags, new object[] { $"{DockerBaseUrl}/{RpsCmdServicePath}/{ActuatorPath}/{HealthPath}" })
            .AddApiHealth("RPS Game Query Microservice Health Check", ApiTags, new object[] { $"{DockerBaseUrl}/{RpsQryServicePath}/{ActuatorPath}/{HealthPath}" })
            .AddApiHealth("Score Command Microservice Health Check", ApiTags, new object[] { $"{DockerBaseUrl}/{ScoreCmdServicePath}/{ActuatorPath}/{HealthPath}" })
            .AddApiHealth("Score Query Microservice Health Check", ApiTags, new object[] { $"{DockerBaseUrl}/{ScoreQryServicePath}/{ActuatorPath}/{HealthPath}" });

        services
            .AddHealthChecksUI()
            .AddInMemoryStorage();
    }
    public void Configure(IApplicationBuilder app, IWebHostEnvironment env, ILoggerFactory loggerFactory)
    {
        //loggerFactory.AddAzureWebAppDiagnostics();
        //loggerFactory.AddApplicationInsights(app.ApplicationServices, LogLevel.Trace);

        if (env.IsDevelopment())
        {
            app.UseDeveloperExceptionPage();
        }
        else
        {
            app.UseExceptionHandler("/Home/Error");
        }

        app.UsePathBase("/status");

        app.UseHttpsRedirection();

        app.UseCors(options => options.AllowAnyOrigin().AllowAnyMethod().AllowAnyHeader());

        app.UseForwardedHeaders(new ForwardedHeadersOptions
        {
            ForwardedHeaders = ForwardedHeaders.XForwardedFor | ForwardedHeaders.XForwardedProto
        });

        app.UseStaticFiles();

        app.UseAuthentication();

        app.UseRouting();

        app.UseEndpoints(endpoints =>
           {
               endpoints.MapDefaultControllerRoute();
               endpoints.MapHealthChecks("/hc", new HealthCheckOptions
               {
                   Predicate = _ => true,
                   ResponseWriter = UIResponseWriter.WriteHealthCheckUIResponse,
                   AllowCachingResponses = false
               });
               endpoints.MapHealthChecksUI(setup =>
               {
                   setup.UIPath = "/hc-ui"; // this is ui path in your browser
                   setup.ApiPath = "/hc-api"; // the UI ( spa app )  use this path to get information from the store ( this is NOT the healthz path, is internal ui api )
               });
           });
    }

    private void RegisterAppInsights(IServiceCollection services)
    {
        services.AddApplicationInsightsTelemetry(Configuration);
        services.AddApplicationInsightsKubernetesEnricher();
    }
}

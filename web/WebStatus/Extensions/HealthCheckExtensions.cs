using WebStatus.HealthChecks;

namespace WebStatus.Extensions
{
    public static class HealthCheckExtensions
    {
        public static IHealthChecksBuilder AddApiHealth(this IHealthChecksBuilder healthChecksBuilder, string name, IEnumerable<string> tags, object[] args)
        {
            return healthChecksBuilder.AddTypeActivatedCheck<ApiHealthCheck>(name,
                failureStatus: HealthStatus.Unhealthy,
                tags: tags,
                args: args);
        }
    }
}

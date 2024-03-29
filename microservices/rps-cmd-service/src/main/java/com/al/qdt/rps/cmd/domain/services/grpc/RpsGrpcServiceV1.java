package com.al.qdt.rps.cmd.domain.services.grpc;

import com.al.qdt.rps.cmd.domain.services.RpsServiceV2;
import com.al.qdt.rps.cmd.domain.services.security.AuthenticationService;
import com.al.qdt.rps.grpc.v1.services.DeleteGameByIdRequest;
import com.al.qdt.rps.grpc.v1.services.DeleteGameByIdResponse;
import com.al.qdt.rps.grpc.v1.services.GameRequest;
import com.al.qdt.rps.grpc.v1.services.GameResponse;
import com.al.qdt.rps.grpc.v1.services.RpsCmdServiceGrpc;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * RPS Game gRPC service implementation class.
 *
 * @version 1
 */
@Slf4j
@GrpcService
@RequiredArgsConstructor
public class RpsGrpcServiceV1 extends RpsCmdServiceGrpc.RpsCmdServiceImplBase {
    private final RpsServiceV2 rpsService;
    private final AuthenticationService authenticationService;

    private static final Set<StreamObserver<GameResponse>> BI_OBSERVERS = new LinkedHashSet<>();

    /**
     * Play game unary rpc service.
     *
     * @param request          game round user inputs
     * @param responseObserver game result
     */
    @Override
    public void play(GameRequest request, StreamObserver<GameResponse> responseObserver) {
        log.info("UNARY GRPC SERVICE: Playing game...");
        // we use the response observer’s onNext() method to return the result
        responseObserver.onNext(this.processGame(request));
        // we use the response observer’s onCompleted() method to specify that we’ve finished dealing with the RPC
        responseObserver.onCompleted();
        log.info("Unary rpc call completed...");
    }

    /**
     * Play game client-to-server streaming rpc service.
     *
     * @param responseObserver response observer object
     * @return response observer
     */
    @Override
    public StreamObserver<GameRequest> playClientStreaming(StreamObserver<GameResponse> responseObserver) {
        log.info("CLIENT-SERVER GRPC SERVICE: Playing game...");
        return new StreamObserver<>() {
            @Override
            public void onNext(GameRequest gameRequest) {
                log.info("Received game inputs: {}", gameRequest);
                responseObserver.onNext(processGame(gameRequest));
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("Error occurred: ", throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
                log.info("Client to server stream completed...");
            }
        };
    }

    /**
     * Play game server-to-client streaming rpc service.
     *
     * @param request               game request
     * @param plainResponseObserver response observer object
     */
    @Override
    public void playServerStreaming(GameRequest request, StreamObserver<GameResponse> plainResponseObserver) {
        log.info("SERVER-CLIENT GRPC SERVICE: Playing game...");
        final var responseObserver =
                (ServerCallStreamObserver<GameResponse>) plainResponseObserver;
        // enables compression for the response
        responseObserver.setCompression("gzip");
        responseObserver.onNext(this.processGame(request));
        responseObserver.onCompleted();
        log.info("Server to client stream completed...");
    }

    /**
     * Play game bidirectional streaming rpc service.
     *
     * @param responseObserver response observer object
     * @return response observer object
     */
    @Override
    public StreamObserver<GameRequest> playBidirectionalStreaming(StreamObserver<GameResponse> responseObserver) {
        log.info("BIDIRECTIONAL GRPC SERVICE: Playing game...");
        // Add response observer to the set
        BI_OBSERVERS.add(responseObserver);
        return new StreamObserver<>() {
            @Override
            public void onNext(GameRequest gameRequest) {
                log.info("Received game inputs: {}", gameRequest);
                BI_OBSERVERS.forEach(o -> o.onNext(processGame(gameRequest)));
            }

            @Override
            public void onError(Throwable throwable) {
                BI_OBSERVERS.remove(responseObserver);
                log.error("Error occurred: ", throwable);
            }

            @Override
            public void onCompleted() {
                BI_OBSERVERS.remove(responseObserver);
                log.info("Bidirectional stream completed...");
            }
        };
    }

    /**
     * Deletes game by id unary rpc service.
     *
     * @param request          delete game by id request
     * @param responseObserver operation response
     */
    @Override
    public void deleteById(DeleteGameByIdRequest request, StreamObserver<DeleteGameByIdResponse> responseObserver) {
        final var gameId = UUID.fromString(request.getId());
        log.info("UNARY GRPC SERVICE: Deleting game by id: {}.", gameId.toString());
        this.rpsService.deleteById(gameId, this.getUserId());
        // we use the response observer’s onNext() method to return the result
        responseObserver.onNext(DeleteGameByIdResponse.newBuilder().build());
        // we use the response observer’s onCompleted() method to specify that we’ve finished dealing with the RPC
        responseObserver.onCompleted();
        log.info("Unary rpc call completed...");
    }

    /**
     * Processes game round inputs.
     *
     * @param request game round inputs
     * @return game result
     */
    private GameResponse processGame(GameRequest request) {
        return GameResponse.newBuilder()
                .setResult(this.rpsService.play(request, this.getUserId()))
                .build();
    }

    /**
     * Returns currently logged in user id.
     *
     * @return user id
     */
    private UUID getUserId() {
        return this.authenticationService.getUserId();
    }
}

package com.al.qdt.rps.qry.domain.services.grpc;

import com.al.qdt.rps.grpc.v1.services.ListOfGamesAdminResponse;
import com.al.qdt.rps.grpc.v1.services.ListOfGamesRequest;
import com.al.qdt.rps.grpc.v1.services.RpsQryServiceGrpc;
import com.al.qdt.rps.qry.domain.services.RpsServiceV2;
import com.al.qdt.rps.qry.domain.services.security.AuthenticationService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

/**
 * RPS Game gRPC API service implementation class.
 *
 * @version 1
 */
@Slf4j
@GrpcService
@RequiredArgsConstructor
public class RpsGrpcServiceV1 extends RpsQryServiceGrpc.RpsQryServiceImplBase {
    private final RpsServiceV2 rpsService;
    private final AuthenticationService authenticationService;

    /**
     * Returns all games unary rpc service.
     *
     * @param request          request
     * @param responseObserver collection of all games
     */
    @Override
    public void listOfGames(ListOfGamesRequest request, StreamObserver<ListOfGamesAdminResponse> responseObserver) {
        if (request.hasUserId()) {
            log.info("UNARY GRPC SERVICE: Finding game by userId: {}.", request.getUserId());
        } else {
            log.info("UNARY GRPC SERVICE: Getting all games...");
        }
        responseObserver.onNext(this.rpsService.all(request.getCurrentPage().getValue(), request.getPageSize().getValue(), request.getSortBy().getValue(), request.getSortingOrder()));
        responseObserver.onCompleted();
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

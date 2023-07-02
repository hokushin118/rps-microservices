package com.al.qdt.rps.cmd.domain.services.grpc;

import com.al.qdt.rps.cmd.domain.services.DbServiceV2;
import com.al.qdt.rps.cmd.domain.services.security.AuthenticationService;
import com.al.qdt.rps.grpc.v1.services.AdminCmdServiceGrpc;
import com.al.qdt.rps.grpc.v1.services.RestoreDbRequest;
import com.al.qdt.rps.grpc.v1.services.RestoreDbResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

/**
 * Database gRPC API server service implementation class.
 * @version 1
 */
@Slf4j
@GrpcService
@RequiredArgsConstructor
public class DbGrpcServiceV1 extends AdminCmdServiceGrpc.AdminCmdServiceImplBase {
    private final DbServiceV2 dbService;
    private final AuthenticationService authenticationService;

    /**
     * Restores database unary rpc service.
     *
     * @param request          request
     * @param responseObserver result
     */
    @Override
    public void restoreDb(RestoreDbRequest request, StreamObserver<RestoreDbResponse> responseObserver) {
        log.info("UNARY GRPC SERVICE: Restoring database...");
        this.dbService.restoreDb(this.getUserId());
        // we use the response observer’s onNext() method to return the result
        responseObserver.onNext(RestoreDbResponse.newBuilder().build());
        // we use the response observer’s onCompleted() method to specify that we’ve finished dealing with the RPC
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

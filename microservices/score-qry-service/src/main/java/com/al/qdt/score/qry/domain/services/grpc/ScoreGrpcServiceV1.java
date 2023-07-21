package com.al.qdt.score.qry.domain.services.grpc;

import com.al.qdt.rps.grpc.v1.services.FindMyScoresRequest;
import com.al.qdt.rps.grpc.v1.services.FindScoreByIdRequest;
import com.al.qdt.rps.grpc.v1.services.FindScoreByUserIdAndWinnerRequest;
import com.al.qdt.rps.grpc.v1.services.FindScoreByUserIdRequest;
import com.al.qdt.rps.grpc.v1.services.FindScoreByWinnerRequest;
import com.al.qdt.rps.grpc.v1.services.ListOfScoresAdminResponse;
import com.al.qdt.rps.grpc.v1.services.ListOfScoresRequest;
import com.al.qdt.rps.grpc.v1.services.ListOfScoresResponse;
import com.al.qdt.rps.grpc.v1.services.ScoreAdminResponse;
import com.al.qdt.rps.grpc.v1.services.ScoreQryServiceGrpc;
import com.al.qdt.score.qry.domain.services.ScoreServiceV2;
import com.al.qdt.score.qry.domain.services.security.AuthenticationService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

/**
 * Score gRPC API service implementation class.
 *
 * @version 1
 */
@Slf4j
@GrpcService
@RequiredArgsConstructor
public class ScoreGrpcServiceV1 extends ScoreQryServiceGrpc.ScoreQryServiceImplBase {
    private final ScoreServiceV2 scoreService;
    private final AuthenticationService authenticationService;

    /**
     * Returns all scores for admin users unary rpc service.
     *
     * @param request          request
     * @param responseObserver collection of all scores
     */
    @Override
    public void listOfScores(ListOfScoresRequest request, StreamObserver<ListOfScoresAdminResponse> responseObserver) {
        log.info("UNARY GRPC SERVICE: Getting all scores.");
        responseObserver.onNext(this.scoreService.all(request.getCurrentPage().getValue(), request.getPageSize().getValue(), request.getSortBy().getValue(), request.getSortingOrder()));
        responseObserver.onCompleted();
    }

    /**
     * Find score for admin users by id.
     *
     * @param request          request
     * @param responseObserver found score
     */
    @Override
    public void findById(FindScoreByIdRequest request, StreamObserver<ScoreAdminResponse> responseObserver) {
        final var scoreId = UUID.fromString(request.getId());
        log.info("UNARY GRPC SERVICE: Finding scores by id: {}.", scoreId.toString());
        final var scoreAdminDto = this.scoreService.findById(scoreId);
        final var scoreAdminResponse = ScoreAdminResponse.newBuilder()
                .setScore(scoreAdminDto)
                .build();
        responseObserver.onNext(scoreAdminResponse);
        responseObserver.onCompleted();
    }

    /**
     * Find scores for admin users by user id.
     *
     * @param request          request
     * @param responseObserver collection of scores
     */
    @Override
    public void findByUserId(FindScoreByUserIdRequest request, StreamObserver<ListOfScoresAdminResponse> responseObserver) {
        final var userId = UUID.fromString(request.getUserId());
        log.info("UNARY GRPC SERVICE: Finding scores by userId: {}.", userId);
        responseObserver.onNext(this.scoreService.findByUserId(userId, request.getCurrentPage().getValue(), request.getPageSize().getValue(), request.getSortBy().getValue(), request.getSortingOrder()));
        responseObserver.onCompleted();
    }

    /**
     * Find scores for admin users by winner.
     *
     * @param request          request
     * @param responseObserver collection of scores
     */
    @Override
    public void findByWinner(FindScoreByWinnerRequest request, StreamObserver<ListOfScoresAdminResponse> responseObserver) {
        log.info("UNARY GRPC SERVICE: Finding scores by winner: {}.", request.getWinner());
        responseObserver.onNext(this.scoreService.findByWinner(request.getWinner(), request.getCurrentPage().getValue(), request.getPageSize().getValue(), request.getSortBy().getValue(), request.getSortingOrder()));
        responseObserver.onCompleted();
    }

    /**
     * Find scores for admin users by user id and winner.
     *
     * @param request          request
     * @param responseObserver collection of scores
     */
    @Override
    public void findByUserIdAndWinner(FindScoreByUserIdAndWinnerRequest request, StreamObserver<ListOfScoresAdminResponse> responseObserver) {
        final var userId = UUID.fromString(request.getUserId());
        log.info("UNARY GRPC SERVICE: Finding scores by user id : {} and winner: {}.", userId, request.getWinner());
        responseObserver.onNext(this.scoreService.findByUserIdAndWinner(userId, request.getWinner(), request.getCurrentPage().getValue(), request.getPageSize().getValue(), request.getSortBy().getValue(), request.getSortingOrder()));
        responseObserver.onCompleted();
    }

    /**
     * Find my scores.
     *
     * @param request          request
     * @param responseObserver collection of scores
     */
    @Override
    public void findMyScores(FindMyScoresRequest request, StreamObserver<ListOfScoresResponse> responseObserver) {
        log.info("UNARY GRPC SERVICE: Finding my scores...");
        responseObserver.onNext(this.scoreService.findMyScores(this.getUserId(), request.getCurrentPage().getValue(), request.getPageSize().getValue(), request.getSortBy().getValue(), request.getSortingOrder()));
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

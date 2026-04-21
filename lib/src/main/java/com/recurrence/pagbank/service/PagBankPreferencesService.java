package com.recurrence.pagbank.service;

import com.recurrence.contract.PreferencesService;
import com.recurrence.contract.domain.NotificationPreferences;
import com.recurrence.contract.domain.PublicKey;
import com.recurrence.contract.domain.RetryPreferences;
import com.recurrence.contract.infra.RequestResponse;
import com.recurrence.core.BaseService;
import com.recurrence.pagbank.PagBankEnvironment;
import com.recurrence.pagbank.client.PagBankPreferencesClient;
import com.recurrence.pagbank.domain.commons.PagBankPreferencesNotification;
import com.recurrence.pagbank.domain.commons.PagBankPublicKey;
import com.recurrence.pagbank.domain.commons.PagBankReferencesRetry;
import com.recurrence.pagbank.domain.error.PagBankErrorHandler;
import com.recurrence.pagbank.mapper.PagBankPreferencesMapper;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class PagBankPreferencesService extends BaseService implements PreferencesService {

    private final String token;
    private final PagBankPreferencesClient publicPreferencesClient;

    public PagBankPreferencesService(
            final PagBankEnvironment environment,
            final String token
    ) {
        this.token = Objects.requireNonNull(token, "Token must be a valid value");
        this.publicPreferencesClient = new PagBankPreferencesClient(Objects.requireNonNull(environment, "Environment must be a valid value"));
    }

    @Override
    public RequestResponse<PublicKey> getPublicKey() {
        try {
            var headers = defaultHeaders(token);
            HttpResponse<String> response = publicPreferencesClient.getKey(headers);
            RequestResponse<PublicKey> requestResponse = buildResponse(response);

            return switch (requestResponse.getHttpResponse()) {
                case 200 -> requestResponse.setBody(PagBankPreferencesMapper.toContract(fromJson(response.body(), PagBankPublicKey.class)));
                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<PublicKey> putPublicKey() {
        try {
            var headers = defaultHeaders(token);
            HttpResponse<String> response = publicPreferencesClient.putKeys(headers);
            RequestResponse<PublicKey> requestResponse = buildResponse(response);

            return switch (requestResponse.getHttpResponse()) {
                case 201 -> requestResponse.setBody(PagBankPreferencesMapper.toContract(fromJson(response.body(), PagBankPublicKey.class)));
                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<NotificationPreferences> getNotificationPreferences() {
        try {
            var headers = defaultHeaders(token);
            HttpResponse<String> response = publicPreferencesClient.getNotifications(headers);
            RequestResponse<NotificationPreferences> requestResponse = buildResponse(response);

            return switch (requestResponse.getHttpResponse()/100) {
                case 2 -> requestResponse.setBody(PagBankPreferencesMapper.toContract(fromJson(response.body(), PagBankPreferencesNotification.class)));
                case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<NotificationPreferences> updateNotificationPreferences(NotificationPreferences notificationPreferences) {
        try {
            var headers = defaultHeaders(token);
            String payload = toJson(PagBankPreferencesMapper.toDomain(notificationPreferences));
            HttpResponse<String> response = publicPreferencesClient.putNotification(payload, headers);
            RequestResponse<NotificationPreferences> requestResponse = buildResponse(response);

            return switch (requestResponse.getHttpResponse()/100) {
                case 2 -> requestResponse.setBody(PagBankPreferencesMapper.toContract(fromJson(response.body(), PagBankPreferencesNotification.class)));
                case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<RetryPreferences> getRetryPreferences() {
        try {
                var headers = defaultHeaders(token);
                HttpResponse<String> response = publicPreferencesClient.getRetry(headers);
                RequestResponse<RetryPreferences> requestResponse = buildResponse(response);

                return switch (requestResponse.getHttpResponse()/100) {
                    case 2 -> requestResponse.setBody(PagBankPreferencesMapper.toContract(fromJson(response.body(), PagBankReferencesRetry.class)));
                    case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                    default -> requestResponse;
                };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<RetryPreferences> updateRetryPreferences(RetryPreferences retryPreferences) {
        try {
            var headers = defaultHeaders(token);
            String payload = toJson(PagBankPreferencesMapper.toDomain(retryPreferences));
            HttpResponse<String> response = publicPreferencesClient.putNotification(payload, headers);
            RequestResponse<RetryPreferences> requestResponse = buildResponse(response);

            return switch (requestResponse.getHttpResponse()/100) {
                case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

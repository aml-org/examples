package utils;

import amf.apicontract.client.platform.model.domain.*;
import amf.apicontract.client.platform.model.domain.api.Api;
import amf.apicontract.client.platform.model.domain.api.AsyncApi;
import amf.apicontract.client.platform.model.domain.api.WebApi;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.model.document.Document;
import amf.core.client.platform.model.domain.DomainElement;

import java.util.List;

public class BaseUnitUtilsJava {
    public static Api getApi(BaseUnit baseUnit, Boolean isAsyncApi) {
        Document encodes = (Document) baseUnit;
        if (isAsyncApi) {
            return (AsyncApi) encodes.encodes();
        } else return (WebApi) encodes.encodes();
    }

    public static List<DomainElement> getDeclarations(BaseUnit baseUnit) {
        return ((Document) baseUnit).declares();
    }

    public static Server getFirstServer(BaseUnit baseUnit, Boolean isAsyncApi) {
        return (Server) getApi(baseUnit, isAsyncApi).servers().get(0);
    }

    public static Server getLastServer(BaseUnit baseUnit, Boolean isAsyncApi) {
        List<Server> servers = getApi(baseUnit, isAsyncApi).servers();
        return servers.get(servers.size() - 1);
    }

    public static EndPoint getLastEndPoint(BaseUnit baseUnit, Boolean isAsyncApi) {
        List<EndPoint> endPoints = getApi(baseUnit, isAsyncApi).endPoints();
        return endPoints.get(endPoints.size() - 1);
    }

    public static Operation getFirstOperationFromLastEndpoint(BaseUnit baseUnit, Boolean isAsyncApi) {
        return getLastEndPoint(baseUnit, isAsyncApi).operations().get(0);
    }

    public static Request getFirstRequest(BaseUnit baseUnit, Boolean isAsyncApi) {
        return getFirstOperationFromLastEndpoint(baseUnit, isAsyncApi).requests().get(0);
    }

    public static Message getMessageComponent(BaseUnit baseUnit) {
        return (Message) getDeclarations(baseUnit).get(2);
    }
}


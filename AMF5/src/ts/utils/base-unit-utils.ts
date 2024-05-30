import {
  Api,
  AsyncApi,
  BaseUnit,
  Document,
  DomainElement,
  EndPoint,
  Message,
  Operation,
  Request,
  Server,
  WebApi
} from 'amf-client-js';

export class BaseUnitUtils {
  public static getApi(baseUnit: BaseUnit, isAsyncApi: boolean = true): Api<any> {
    if (isAsyncApi) {
      return (baseUnit as Document).encodes as AsyncApi;
    } else return (baseUnit as Document).encodes as WebApi;
  }

  public static getDeclarations(baseUnit: BaseUnit): Array<DomainElement> {
    return (baseUnit as Document).declares;
  }

  public static getFirstServer(baseUnit: BaseUnit, isAsyncApi: boolean = true): Server {
    return this.getApi(baseUnit, isAsyncApi).servers.at(0);
  }

  public static getLastServer(baseUnit: BaseUnit, isAsyncApi: boolean = true): Server {
    const servers: Array<Server> = this.getApi(baseUnit, isAsyncApi).servers;
    return servers.at(servers.length - 1);
  }

  public static getLastEndpoint(baseUnit: BaseUnit, isAsyncApi: boolean = true): EndPoint {
    const endpoints: Array<EndPoint> = this.getApi(baseUnit, isAsyncApi).endPoints;
    return endpoints.at(endpoints.length - 1);
  }

  public static getFirstOperationFromLastEndpoint(baseUnit: BaseUnit, isAsyncApi: boolean = true): Operation {
    return this.getLastEndpoint(baseUnit, isAsyncApi).operations.at(0);
  }

  public static getFirstRequest(baseUnit: BaseUnit, isAsyncApi: boolean = true): Request {
    return this.getFirstOperationFromLastEndpoint(baseUnit, isAsyncApi).requests.at(0);
  }

  public static getMessageComponent(baseUnit: BaseUnit): Message {
    return this.getDeclarations(baseUnit).at(2) as Message;
  }
}
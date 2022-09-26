
import {
    AMFParseResult,
    OASConfiguration,
    ComponentModule,
    ClientUnitCacheAdapter,
    CachedReference,
    BaseUnit,
    Document,
    Api,
    ClientUnitCache,
    ResourceNotFound
} from 'amf-client-js';
import {expect} from 'chai';

describe('Oas 3 Components Usage', () => {

    const apiPath = "file://src/test/resources/examples/components/api.yaml"
    const componentsPath = "file://src/test/resources/examples/components/oas-3-component.yaml"
    const aliasPath = "file://src/test/resources/examples/components/myComponents.yaml"

    it('usages an OAS 3 Component library via cache', async () => {
        const client = OASConfiguration.OAS30Component().baseUnitClient()
        const parseResult: AMFParseResult = await client.parse(componentsPath);
        expect(parseResult.conforms).to.be.true;
        const components: ComponentModule = parseResult.baseUnit as ComponentModule

        const nativeCache = new TestCache().add(aliasPath, componentsPath, components);
        const cache = ClientUnitCacheAdapter.adapt(nativeCache)

        const apiClient = OASConfiguration.OAS30().withUnitCache(cache).baseUnitClient()
        const parsedApi = await apiClient.parse(apiPath)
        expect(parsedApi.conforms).to.be.true;
        expect(nativeCache.getCacheHits()).to.contain(aliasPath)

        const doc = parsedApi.baseUnit as Document
        const api = doc.encodes as Api<any>
        const response200 = api.endPoints[0].operations[0].responses[0].payloads[0]
        expect(response200.examples[0].isLink).to.be.true
        expect(response200.schema.isLink).to.be.true
    });

});



class TestCache implements ClientUnitCache {

    private cache: Map<string, CachedReference>
    private hits: string[]

    constructor(cache: Map<string, CachedReference> = new Map<string, CachedReference>) {
        this.cache = cache
        this.hits = []
    }

    getCacheHits(): string[] {
        return this.hits
    }

    add(key: string, url: string, unit: BaseUnit): TestCache {
        const next = this.copy(this.cache).set(key, new CachedReference(url, unit))
        return new TestCache(next)
    }

    fetch(url: string): Promise<CachedReference> {
        if (this.cache.has(url)) {
            this.hits = this.hits.concat(url)
            return Promise.resolve(this.cache.get(url))
        } else return Promise.reject(new ResourceNotFound(url))
    }

    private copy<K, V>(map: Map<K, V>): Map<K, V> {
        const result = new Map<K, V>()
        map.forEach((value, key) => {
            result.set(key, value)
        })
        return result
    }
}

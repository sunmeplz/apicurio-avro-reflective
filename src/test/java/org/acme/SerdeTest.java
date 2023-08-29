package org.acme;


import io.apicurio.registry.resolver.SchemaResolverConfig;
import io.apicurio.registry.serde.avro.AvroKafkaSerdeConfig;
import io.apicurio.registry.serde.avro.AvroSerde;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@QuarkusTest
public class SerdeTest {
    public static final Logger log = LoggerFactory.getLogger(SerdeTest.class);


    static AvroSerde<MyPojo> serde;

    @BeforeAll
    public static void prepare() {

        String url = ConfigProvider.getConfig()
            .getValue("mp.messaging.connector.smallrye-kafka.apicurio.registry.url", String.class);
        log.info("Apicurio url:" + url);
        serde = new AvroSerde();
        Map<String, Object> config = Map.of(
            AvroKafkaSerdeConfig.AVRO_DATUM_PROVIDER, "io.apicurio.registry.serde.avro.ReflectAvroDatumProvider",
            AvroKafkaSerdeConfig.USE_SPECIFIC_AVRO_READER,
            "true",
            SchemaResolverConfig.ARTIFACT_RESOLVER_STRATEGY,
            "io.apicurio.registry.serde.avro.strategy.QualifiedRecordIdStrategy",
            SchemaResolverConfig.AUTO_REGISTER_ARTIFACT,
            "true",
            SchemaResolverConfig.REGISTRY_URL,
            url
        );
        serde.configure(config, false);
    }

    @Test
    public void test() {
        MyPojo pojo = new MyPojo();
        pojo.id = 1L;
        pojo.title = "String";
        pojo.myEnum = MyEnum.OPTION_1;

        byte[] bytes = serde.serializer().serialize("mytopic", pojo);

        MyPojo deserialized = serde.deserializer().deserialize("mytopic", bytes);

        log.info(deserialized.toString());
    }

}

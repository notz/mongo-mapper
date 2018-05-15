package eu.dozd.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import eu.dozd.mongo.crypto.*;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.util.*;

public class AbstractMongoIT {
    protected MongoDatabase db;
    protected String dbName;
    protected MongoClient client;

    @Before
    public void setUp() throws Exception {
        CodecRegistry codecRegistry = CodecRegistries.fromProviders(MongoMapper.getProviders());
        MongoMapper.setCryptoProvider(
                new AesCbcCryptoProvider(Base64.getDecoder()
                        .decode("hqHKBLV83LpCqzKpf8OvutbCs+O5wX5BPu3btWpEvXA=")
                )
        );

        MongoClientOptions settings = MongoClientOptions.builder().codecRegistry(codecRegistry).build();

        String port = System.getProperty("embedMongoPort");
        Assert.assertNotNull("Please, set system property 'embedMongoPort' to run this test outside Maven.", port);

        client = new MongoClient(new ServerAddress("127.0.0.1", Integer.parseInt(port)), settings);
        dbName = "mapper_test" + UUID.randomUUID();
        db = client.getDatabase(dbName);
    }

    @After
    public void tearDown() throws Exception {
        if (client != null) {
            client.dropDatabase(dbName);
            client.close();
        }
    }
}

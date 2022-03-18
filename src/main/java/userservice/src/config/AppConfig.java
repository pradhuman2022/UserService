package userservice.src.config;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import userservice.src.security.Secure;

@Slf4j
@Configuration
public class AppConfig {
    
    @Value("${stage}")
    String prefix;

    @Bean
    AmazonDynamoDB dynamoDBClient() {
        EndpointConfiguration config = new EndpointConfiguration("http://localhost:8000/", "us-east-1");
        AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(config).build();
        return ddb;
    }

    @Bean
    DynamoDB dynamoDB (AmazonDynamoDB amazonDynamoDBClient) {
        return new DynamoDB(amazonDynamoDBClient);
    }

    @Bean
    DynamoDBMapperConfig dynamoDBMapperConfig() {
        //can be pulled from a dynamic logic eg: profile, env variable etc
        log.info(prefix + " prefix");
        return new DynamoDBMapperConfig.Builder()
                .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNamePrefix(""))
                .build();
    }

    @Bean
    DynamoDBMapper createDbMapper (AmazonDynamoDB dynamoDB, DynamoDBMapperConfig dynamoDBMapperConfig) {
        return new DynamoDBMapper(dynamoDB, dynamoDBMapperConfig);
    }

    @Bean
    ObjectMapper getObjectMapper() {
        return new ObjectMapper();       
    }

    @Bean
    Secure getSecure() {
        return new Secure();
    }
}

package ee.omnifish.superfast.plainjava.infra;

import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;


public class PlainJavaFunctionInfra {
    public static void main(final String[] args) {
        App app = new App();

        new PlainJavaFunctionAwsStack(app, "PlainJavaFunctionStack", StackProps.builder()
                .build());

        app.synth();
    }
}


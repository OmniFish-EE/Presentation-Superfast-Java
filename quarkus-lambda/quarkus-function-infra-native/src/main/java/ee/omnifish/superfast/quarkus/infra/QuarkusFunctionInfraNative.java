package ee.omnifish.superfast.quarkus.infra;

import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;


public class QuarkusFunctionInfraNative {
    public static void main(final String[] args) {
        App app = new App();

        new QuarkusFunctionNativeAwsStack(app, "QuarkusFunctionNativeStack", StackProps.builder()
                .build());

        app.synth();
    }
}


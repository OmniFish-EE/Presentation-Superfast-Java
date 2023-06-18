package org.acme.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import jakarta.inject.Inject;

public class GreetingLambda implements RequestHandler<Person, String> {

    @Inject
    Person person;
    
    @Override
    public String handleRequest(Person input, Context context) {
        return "Hello " + input.getName();
    }
}

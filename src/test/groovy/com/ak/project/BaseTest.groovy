package com.ak.project

import com.ak.project.user.UserServiceClient
import spock.lang.Specification

abstract class BaseTest extends Specification {
    static final String ENV = System.getenv("ENV") != null ? System.getenv("ENV").toLowerCase() : "dev"
    static final String ACCESS_TOKEN = System.getenv("TOKEN") != null ? System.getenv("TOKEN").toLowerCase() : "dev"



    // List of all URLs to test
    static def urls = [
            "dev": [
                    gorest :   'https://gorest.co.in/public-api'

            ]
    ]

    static UserServiceClient userServiceClient

    def "setupSpec"() {
        if (ENV == null || ENV.blank) {
            assert false: "Missing a command line ENV system variable"
        }
        if (urls[ENV] == null) {
            assert false: "Invalid command line ENV system variable. Valid values:${urls.keySet()}"
        }

        userServiceClient = new UserServiceClient(urls[ENV]["gorest"])
    }

    def "cleanup"() {
        userServiceClient.delete(ACCESS_TOKEN)
    }
}

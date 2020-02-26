package com.bisnode.opa.client.rest;

import java.util.Optional;

class UrlNormalizer {
    public static Optional<String> normalize(String input) {
        return Optional.ofNullable(input)
                .map(String::trim)
                .map(UrlNormalizer::removeExtraSlashes)
                .map(UrlNormalizer::removeTrailingSlash);
    }

    private static String removeTrailingSlash(String input) {
        if (input.endsWith("/")) {
            return input.substring(0, input.length() - 1);
        }
        return input;
    }

    private static String removeExtraSlashes(String input) {
        return input.replaceAll("([^:])//+", "$1/");
    }
}

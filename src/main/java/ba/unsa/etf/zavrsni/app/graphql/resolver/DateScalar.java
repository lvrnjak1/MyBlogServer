package ba.unsa.etf.zavrsni.app.graphql.resolver;

import graphql.schema.*;

public class DateScalar {
    public static final GraphQLScalarType Date = new GraphQLScalarType("Date",
            "A custom scalar that handles date type",
            new Coercing() {
                @Override
                public Object serialize(Object dataFetcherResult) throws CoercingSerializeException {
                    return serializeDate(dataFetcherResult);
                }

                @Override
                public Object parseValue(Object input) throws CoercingParseValueException {
                    return parseDateFromValue(input);
                }

                @Override
                public Object parseLiteral(Object input) throws CoercingParseLiteralException {
                    return parseDateFromAstLiteral(input);
                }
            });

    private static Object serializeDate(Object dataFetcherResult) {
        //Long date = Long.parseLong(dataFetcherResult.toString());
        return null;
    }

    private static Object parseDateFromValue(Object input) {
        return null;
    }

    private static Object parseDateFromAstLiteral(Object input) {
        return null;
    }
}

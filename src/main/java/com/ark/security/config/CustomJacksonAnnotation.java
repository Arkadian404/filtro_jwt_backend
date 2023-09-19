package com.ark.security.config;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class CustomJacksonAnnotation extends JacksonAnnotationIntrospector {

    @Override
    public ObjectIdInfo findObjectIdInfo(Annotated ann) {
        return null;
    }

    @Override
    public ObjectIdInfo findObjectReferenceInfo(Annotated ann, ObjectIdInfo objectIdInfo) {
        return null;
    }
}

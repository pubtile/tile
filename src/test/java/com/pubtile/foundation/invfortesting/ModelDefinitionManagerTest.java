package com.pubtile.foundation.invfortesting;

import com.pubtile.foundation.domain.model.meta.definition.ModelDefinition;
import com.pubtile.foundation.domain.model.meta.ModelDefinitionManager;
import com.pubtile.foundation.invfortesting.domain.model.InvForTesting;
import org.junit.jupiter.api.Test;

class ModelDefinitionManagerTest {
    @Test
    void getModelMeta() {
        ModelDefinition modelDefinition = ModelDefinitionManager.getModelMetaDefinition(InvForTesting.class);
        System.out.println(modelDefinition);
    }
}
package org.needlehack.searchapi

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

class ThrottlingInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
        TestPropertyValues.of(
                "app.limit=2"
        ).applyTo(configurableApplicationContext.environment)
    }
}


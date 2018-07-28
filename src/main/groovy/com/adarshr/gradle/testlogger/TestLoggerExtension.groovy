package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.theme.ThemeType
import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.logging.configuration.ConsoleOutput

import static com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN
import static com.adarshr.gradle.testlogger.theme.ThemeType.STANDARD
import static org.gradle.api.logging.configuration.ConsoleOutput.Plain

@CompileStatic
class TestLoggerExtension {

    ThemeType theme = STANDARD
    boolean showExceptions = true
    long slowThreshold = 2000
    boolean showSummary = true
    boolean showStandardStreams = false

    private final ConsoleOutput consoleType
    private final Map<String, String> overrides

    TestLoggerExtension(Project project, Map<String, String> overrides) {
        this.consoleType = project.gradle.startParameter.consoleOutput
        this.theme = project.gradle.startParameter.consoleOutput == Plain ? PLAIN : this.theme
        this.overrides = overrides
    }

    void setTheme(String theme) {
        if (consoleType == Plain) {
            return
        }

        this.theme = ThemeType.fromValue(theme)
    }

    void setTheme(ThemeType theme) {
        if (consoleType == Plain) {
            return
        }

        this.theme = theme
    }

    void applyOverrides() {
        override('theme', ThemeType)
        override('showExceptions', Boolean)
        override('slowThreshold', Long)
        override('showSummary', Boolean)
        override('showStandardStreams', Boolean)
    }

    private void override(String name, Class type) {
        if (overrides.containsKey(name)) {
            String method = Enum.isAssignableFrom(type) ? 'fromValue' : 'valueOf'

            setProperty(name, type.invokeMethod(method, overrides[name]))
        }
    }
}

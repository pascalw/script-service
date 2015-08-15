package nl.pwiddershoven.script.service;

import static org.junit.Assert.assertEquals;

import nl.pwiddershoven.script.service.script.ScriptExecutor;
import nl.pwiddershoven.script.service.script.module.JsModule;
import nl.pwiddershoven.script.service.script.module.ScriptExecutionException;

import org.junit.Test;

import com.google.common.collect.Sets;

public class ScriptExecutorTest {
    public class MyModule implements JsModule {
        @Override
        public String name() {
            return "myModule";
        }

        public String hello() {
            return "world";
        }
    }

    private ScriptExecutor scriptExecutor = new ScriptExecutor();

    @Test
    public void executes_scripts() {
        ScriptConfiguration configuration = buildConfiguration("return 'Hello, world!';");
        assertEquals("Hello, world!", scriptExecutor.execute(configuration));
    }

    @Test
    public void loads_and_exposes_modules_by_name() {
        scriptExecutor.setJsModules(Sets.newHashSet(new MyModule()));

        ScriptConfiguration configuration = buildConfiguration("return require('myModule').hello();");
        assertEquals("world", scriptExecutor.execute(configuration));
    }

    @Test(expected = ScriptExecutionException.class)
    public void throws_when_loading_unknown_module() {
        ScriptConfiguration configuration = buildConfiguration("require('foobar');");
        scriptExecutor.execute(configuration);
    }

    @Test(expected = ScriptExecutionException.class)
    public void throws_on_script_execution_failure() {
        ScriptConfiguration configuration = buildConfiguration("return doesNotExist;");
        scriptExecutor.execute(configuration);
    }

    private ScriptConfiguration buildConfiguration(String script) {
        return new ScriptConfiguration(script, "application/json", null);
    }
}
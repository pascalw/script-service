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
        ScriptConfiguration configuration = new ScriptConfiguration("return 'Hello, world!';", "application/json");
        assertEquals("Hello, world!", scriptExecutor.execute(configuration));
    }

    @Test
    public void loads_and_exposes_modules_by_name() {
        scriptExecutor.setJsModules(Sets.newHashSet(new MyModule()));

        ScriptConfiguration configuration = new ScriptConfiguration("return require('myModule').hello();", "application/json");
        assertEquals("world", scriptExecutor.execute(configuration));
    }

    @Test(expected = ScriptExecutionException.class)
    public void throws_when_loading_unknown_module() {
        ScriptConfiguration configuration = new ScriptConfiguration("require('foobar');", "application/json");
        scriptExecutor.execute(configuration);
    }

    @Test(expected = ScriptExecutionException.class)
    public void throws_on_script_execution_failure() {
        ScriptConfiguration configuration = new ScriptConfiguration("return doesNotExist;", "application/json");
        scriptExecutor.execute(configuration);
    }
}
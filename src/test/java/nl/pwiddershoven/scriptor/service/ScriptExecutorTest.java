package nl.pwiddershoven.scriptor.service;

import static org.junit.Assert.assertEquals;

import nl.pwiddershoven.scriptor.service.script.JsContext;
import nl.pwiddershoven.scriptor.service.script.ScriptExecutor;
import nl.pwiddershoven.scriptor.service.script.module.*;

import org.junit.Test;

import com.google.common.collect.Sets;

public class ScriptExecutorTest {
    public static class MyModuleProvider implements JsModuleProvider {
        public static class MyModule implements JsModule {
            public String hello() {
                return "world";
            }
        }

        @Override
        public String name() {
            return "myModule";
        }

        @Override
        public JsModule module(JsContext jsContext) {
            return new MyModule();
        }
    }

    private ScriptExecutor scriptExecutor = new ScriptExecutor(Sets.newHashSet(new MyModuleProvider()));

    @Test
    public void executes_scripts() {
        assertEquals("Hello, world!", scriptExecutor.execute("return 'Hello, world!';"));
    }

    @Test
    public void loads_and_exposes_modules_by_name() {
        assertEquals("world", scriptExecutor.execute("return require('myModule').hello();"));
    }

    @Test(expected = ScriptExecutionException.class)
    public void throws_when_loading_unknown_module() {
        scriptExecutor.execute("require('foobar');");
    }

    @Test(expected = ScriptExecutionException.class)
    public void throws_on_script_execution_failure() {
        scriptExecutor.execute("return doesNotExist;");
    }
}

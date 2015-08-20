package nl.pwiddershoven.script.service.script.module.twitter;

import nl.pwiddershoven.script.service.script.ScriptExecutor;
import nl.pwiddershoven.script.service.script.module.JsModuleProvider;

import org.springframework.stereotype.Component;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.twitter.Autolink;

@Component
public class TwitterModuleProvider implements JsModuleProvider {

    public static class TwitterModule implements JsModule {
        private Autolink autolink = new Autolink();

        public Twitter getClient(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
            ConfigurationBuilder cb = new ConfigurationBuilder()
                    .setOAuthConsumerKey(consumerKey)
                    .setOAuthConsumerSecret(consumerSecret)
                    .setOAuthAccessToken(accessToken)
                    .setOAuthAccessTokenSecret(accessTokenSecret);

            return new TwitterFactory(cb.build()).getInstance();
        }

        public String autolink(String tweet) {
            return autolink.autoLink(tweet);
        }
    }

    private TwitterModule module = new TwitterModule();

    @Override
    public String name() {
        return "twitter";
    }

    @Override
    public TwitterModule module(ScriptExecutor.JsContext jsContext) {
        return module;
    }

}

package nl.pwiddershoven.script.service.script.module.twitter;

import nl.pwiddershoven.script.service.script.module.JsModule;

import org.springframework.stereotype.Component;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import com.twitter.Autolink;

@Component
public class TwitterModule implements JsModule {
    private Autolink autolink = new Autolink();

    @Override
    public String name() {
        return "twitter";
    }

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

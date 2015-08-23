package nl.pwiddershoven.scriptor.service.script.module.twitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import twitter4j.Twitter;

public class TwitterModuleProviderTest {
    private TwitterModuleProvider.TwitterModule twitterModule = new TwitterModuleProvider().module(null);

    @Test
    public void getClient_produces_twitter_client_with_provided_configuration() {
        Twitter client = twitterModule.getClient("consumerKey", "consumerSecret", "accessToken", "accessTokenSecret");

        assertEquals("consumerKey", client.getConfiguration().getOAuthConsumerKey());
        assertEquals("consumerSecret", client.getConfiguration().getOAuthConsumerSecret());
        assertEquals("accessToken", client.getConfiguration().getOAuthAccessToken());
        assertEquals("accessTokenSecret", client.getConfiguration().getOAuthAccessTokenSecret());
    }

    @Test
    public void autolink_processes_tweets() {
        String tweetText = "#java";
        assertTrue(twitterModule.autolink(tweetText).contains("hashtag"));
    }

}
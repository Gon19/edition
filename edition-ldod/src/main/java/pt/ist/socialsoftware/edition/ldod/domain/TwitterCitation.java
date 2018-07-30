package pt.ist.socialsoftware.edition.ldod.domain;

public class TwitterCitation extends TwitterCitation_Base {

	public TwitterCitation(Fragment fragment, String sourceLink, String date, String fragText, String tweetText,
			long tweetID, String location, String country, String username, String profURL, String profImgURL) {

		super.init(fragment, sourceLink, date, fragText);
		setTweetText(tweetText);
		setTweetID(tweetID);
		setLocation(location);
		setCountry(country);
		setUsername(username);
		setUserProfileURL(profURL);
		setUserImageURL(profImgURL);
	}

	@Override
	public void remove() {
		getTweetSet().stream().forEach(t -> removeTweet(t));

		super.remove();
	}

	@Override
	public long getId() {
		return getTweetID();
	}

}

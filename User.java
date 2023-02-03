package finalproject;

import java.util.ArrayList;

public class User {
	private String name;
	private int id;
	private ArrayList<String> posts;
	private ArrayList<Integer> followers;
	private ArrayList<Integer> following;
	private ArrayList<String> topics;
	
	public User() {
		// TODO Auto-generated constructor stub
		name = "";
		id = 0;
		posts = new ArrayList<>();
		followers = new ArrayList<>();
		following = new ArrayList<>();
		topics = new ArrayList<>();
	}
	public ArrayList<Integer> getFollowers() {
		return followers;
	}
	public ArrayList<Integer> getFollowing() {
		return following;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public ArrayList<String> getPosts() {
		return posts;
	}
	public ArrayList<String> getTopics() {
		return topics;
	}
	public void setFollowers(ArrayList<Integer> followers) {
		this.followers = followers;
	}
	public void setFollowing(ArrayList<Integer> following) {
		this.following = following;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPosts(ArrayList<String> posts) {
		this.posts = posts;
	}
	public void setTopics(ArrayList<String> topics) {
		this.topics = topics;
	}
	public void addPosts(String post) {
		this.posts.add(post);
	}
	public void addTopics(String topic) {
		this.topics.add(topic);
	}
	public void addFollowers(int id) {
		this.followers.add(id);
	}
	public void addFollowing(int id){
		this.following.add(id);
	}
}

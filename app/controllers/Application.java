package controllers;

import java.util.List;

import models.Post;
import play.cache.Cache;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {
        Post frontPost = Post.find("order by postedAt desc").first();
        System.out.println(frontPost);
        List<Post> olderPosts = Post.find(
            "order by postedAt desc"
        ).from(1).fetch(10);
        render(frontPost, olderPosts);
    }

    public static void captcha(String id) {
    	Images.Captcha captcha = Images.captcha();
    	String code = captcha.getText("#E4EAFD");
    	Cache.set(id, code, "10mn");
    	renderBinary(captcha);
    }

    public static void show(Long id) {
    	Post post = Post.findById(id);
    	String randomID = Codec.UUID();
    	render(post, randomID);
    }

    public static void postComment(
    	Long postId,
    	@Required String author,
   		@Required String content,
    	@Required String code,
    	String randomID
    ) {
    	System.out.println("0."+postId);
    	System.out.println("1."+author);
    	System.out.println("2."+content);
    	System.out.println("3."+code);
    	System.out.println("4."+randomID);
    	Post post = Post.findById(postId);
    	validation.equals(
    		code, Cache.get(randomID)
    	).message("Invalid code. Please type it again");
    	if (validation.hasErrors()) {
    		render("Application/show.html", post);
    	}
    	post.addComment(author, content);
    	flash.success("Thanks for posting %s", author);
    	Cache.delete(randomID);
    	show(postId);
    }
}
package models;

import javax.persistence.Entity;
import play.db.jpa.Model;

@Entity // ← この記載によりJPAヘルパを自動的に提供
public class Tag extends Model implements Comparable<Tag>{
	public String name;
	private Tag(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public int compareTo(Tag otherTag) {
		return name.compareTo(otherTag.name);
	}

	public static Tag findOrCreateByName(String name) {
		Tag tag = Tag.find("byName", name).first();
		if(tag == null) {
			tag = new Tag(name);
		}
		return tag;
	}
}

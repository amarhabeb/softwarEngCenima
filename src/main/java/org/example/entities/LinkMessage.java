package org.example.entities;

import org.example.entities.Message;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="linkmessage")

public class LinkMessage extends Message {
    private Link link;
    public LinkMessage(Link link){
        super("Details of your link with the times it's available:");
        this.link=link;
    }
    public LinkMessage(){super();}
	public Link getLink() {
		return link;
	}
	public void setLink(Link link) {
		this.link = link;
	}
}

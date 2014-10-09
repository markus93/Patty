package com.nortal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "pat")
public class Pat {

    @Id
    public String id;
    public String fromName;
    public String fromUser;
    public String toName;
    public String toUser;
    public String description;
    public Date submitDate;
}

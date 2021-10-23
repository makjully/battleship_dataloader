package ru.levelup.battleship_dataloader.loader;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonDeserialize(as = User.class)
@Getter
@Setter
@NoArgsConstructor
public class User {
    @JsonProperty
    private int id;

    @JsonProperty
    private String login;

    @JsonProperty
    private String password;

    @JsonProperty
    private double rating;

    @JsonIgnore
    private boolean playerFieldArranged;

    public User(int id, String login, String password, double rating) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.rating = rating;
    }
}
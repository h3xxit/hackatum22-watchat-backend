package com.hackatum.watchat.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tag implements HasId<String>{
    private String name;

    @Override
    public String getId() {
        return name;
    }
}

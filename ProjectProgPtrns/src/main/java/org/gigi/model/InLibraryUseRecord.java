package org.gigi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InLibraryUseRecord {
    private User user;

    public InLibraryUseRecord(User user) {
        this.user = user;
    }
}

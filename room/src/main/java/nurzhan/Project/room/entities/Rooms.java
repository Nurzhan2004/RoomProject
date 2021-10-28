package nurzhan.Project.room.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rooms implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Users> users;
    @ManyToOne(fetch = FetchType.LAZY)
    private Countries country;
    @Column(name = "image")
    private String image;
    @Column(name = "url")
    private String url;
    @ManyToOne(fetch = FetchType.LAZY)
    private Users author;
    @Column(name = "color")
    private String color;
}

package com.sunghyun.note.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity 
public class Note {
    
    public static final int TITLE_MAX_LENGTH=100;

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length =TITLE_MAX_LENGTH)
    private String title;

    @Column(nullable = false, length = 10_000)
    private String content;

    @Column(nullable =false, updatable =false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected Note(){

    }
    public Note(String title, String content){
            validateTitle(title);
            this.title = title;
            this.content = (content ==null)? "" : content;

    }  

    private static void validateTitle(String title){
        if(title ==null || title.isBlank()){
            throw new IllegalArgumentException("제목은 비어 있을 수 없습니다.");
        }
        if(title.length()>TITLE_MAX_LENGTH){
            throw new IllegalArgumentException("제목은" +TITLE_MAX_LENGTH + "자 이하여야 합니다.");
        
        }
    }
    @PrePersist
    void onCreate(){
        LocalDateTime now = LocalDateTime.now();
        this.createdAt =now;
        this.updatedAt =now;
    }
@PreUpdate 
void onUpdate(){
    this.updatedAt = LocalDateTime.now();
}
public Long getId(){return id;}
public String getTitle(){return title;}
public String getContent(){return content;}
public LocalDateTime getCreatedAt(){return createdAt;}
public LocalDateTime getUpdatedAt(){return updatedAt;}

}

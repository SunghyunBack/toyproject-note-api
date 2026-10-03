package com.sunghyun.note.domain;


import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

public class NoteTest {
    @Test
    void 제목과_본문으로_노트를_생성한다(){

        Note note = new Note("Spring 공부", "DI란 무엇인가");
        assertThat(note.getTitle()).isEqualTo("Spring 공부");
        assertThat(note.getContent()).isEqualTo("DI란 무엇인가");
    }

    @Test
    void 본문이_null이면_빈_문자열로_저장한다(){
        Note note = new Note("제목", null);
        assertThat(note.getContent()).isEmpty();
    }

    @Test
    void 제목이_null이면_예외가_발생한다(){
        assertThatThrownBy(()-> new Note(null,"본문"))
        .isInstanceOf(IllegalArgumentException.class);
    }
@Test
    void 제목이_공백뿐이면_예외가_발생한다(){
        assertThatThrownBy(()-> new Note("   ","본문"))
        .isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    void 제목은_100자까지_허용한다(){
        String title ="a".repeat(100);
        Note note = new Note(title, "본문");
        assertThat(note.getTitle()).hasSize(100);
    }
@Test
    void 제목은_100자를_넘으면_예외가_발생한다(){
        String title ="a".repeat(101);
        
        assertThatThrownBy(()-> new Note(title, "본문"))
        .isInstanceOf(IllegalArgumentException.class);
    }

        @Test
    void 태그와_함께_노트를_생성한다() {
        Note note = new Note("제목", "본문", Set.of("spring", "jpa"));

        assertThat(note.getTags()).containsExactlyInAnyOrder("spring", "jpa");
    }

    @Test
    void 태그가_null이면_빈_태그로_생성한다() {
        Note note = new Note("제목", "본문", null);

        assertThat(note.getTags()).isEmpty();
    }

    @Test
    void 태그_앞뒤_공백은_제거한다() {
        Note note = new Note("제목", "본문", Set.of("  spring  "));

        assertThat(note.getTags()).containsExactly("spring");
    }

    @Test
    void 공백_제거_후_같은_태그는_하나로_합친다() {
        Note note = new Note("제목", "본문", Set.of("spring", " spring"));

        assertThat(note.getTags()).hasSize(1);
    }

    @Test
    void 공백뿐인_태그는_예외가_발생한다() {
        assertThatThrownBy(() -> new Note("제목", "본문", Set.of("   ")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 태그가_20자를_넘으면_예외가_발생한다() {
        String tag = "a".repeat(21);

        assertThatThrownBy(() -> new Note("제목", "본문", Set.of(tag)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 반환된_태그는_외부에서_수정할_수_없다() {
        Note note = new Note("제목", "본문", Set.of("spring"));

        assertThatThrownBy(() -> note.getTags().add("hack"))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}

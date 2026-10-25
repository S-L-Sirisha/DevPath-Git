package com.devpath.repository;

import com.devpath.entity.Question;
import com.devpath.entity.enums.MistakeCategory;
import com.devpath.entity.enums.SkillCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCategory(SkillCategory category);
    List<Question> findByTopic(String topic);
    List<Question> findByMistakeCategory(MistakeCategory mistakeCategory);
}

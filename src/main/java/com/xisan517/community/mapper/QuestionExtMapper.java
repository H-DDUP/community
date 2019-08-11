package com.xisan517.community.mapper;

import com.xisan517.community.model.Question;
import com.xisan517.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question record);
}
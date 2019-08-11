package com.xisan517.community.service;

import com.xisan517.community.dto.PageinationDTO;
import com.xisan517.community.dto.QuestionDTO;
import com.xisan517.community.exception.CustomizeErrorCode;
import com.xisan517.community.exception.CustomizeException;
import com.xisan517.community.mapper.QuestionExtMapper;
import com.xisan517.community.mapper.QuestionMapper;
import com.xisan517.community.mapper.UserMapper;
import com.xisan517.community.model.Question;
import com.xisan517.community.model.QuestionExample;
import com.xisan517.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    public PageinationDTO list(Integer page, Integer size) {

        PageinationDTO pageinationDTO = new PageinationDTO();
        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());
        Integer totalPage;
        if (totalCount % size == 0){
            totalPage = totalCount /size;

        }else {
            totalPage = totalCount /size+1;
        }
        if (page<1){
            page = 1;
        }
        if(page > totalPage){
            page = totalPage;
        }

        pageinationDTO.setPagination(totalPage,page);

        //size*(page-1)
        Integer offset = size*(page-1);

        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {

            User user =  userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageinationDTO.setQuestions(questionDTOList);

        
        return pageinationDTO;
    }

    public PageinationDTO listByUserId(Integer userId, Integer page, Integer size) {

        PageinationDTO pageinationDTO = new PageinationDTO();

        QuestionExample questionExample  = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);

        Integer totalPage;
        if (totalCount % size == 0){
            totalPage = totalCount /size;

        }else {
            totalPage = totalCount /size+1;
        }
        if (page<1){
            page = 1;
        }
        if(page > totalPage){
            page = totalPage;
        }

        pageinationDTO.setPagination(totalPage,page);
        //size*(page-1)
        Integer offset = size*(page-1);

        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();


        for (Question question : questions) {
            User user =  userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageinationDTO.setQuestions(questionDTOList);


        return pageinationDTO;
    }

    public QuestionDTO getById(Integer id) {

        Question  question =  questionMapper.selectByPrimaryKey(id);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null){
            //新增
            questionMapper.insert(question);
        }else{
            //更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion,example);
            if (updated != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Integer id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);

    }
}

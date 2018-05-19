package codesquad.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class QuestionController {

    private static final Logger log = LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/questions/form")
    public String goForm(HttpSession session) {
        if (!HttpSessionUtils.isLoginUser(session)) {
            return "redirect:/";
        }
        return "/qna/form";
    }

    @PostMapping("/questions")
    public String saveQuestion(Question question, HttpSession session) {
        User user = HttpSessionUtils.getSessionedUser(session);
        question.setWriter(user.getUserId());
        questionRepository.save(question);
        log.debug("Question : {}", question);
        return "redirect:/";
    }

    @GetMapping("/")
    public String goHome(Model model) {
        model.addAttribute("questions", questionRepository.findAll());
        return "index";
    }

    @GetMapping("/questions/{id}")
    public String showQuestion(@PathVariable Long id, Model model) {
        model.addAttribute("question", questionRepository.findOne(id));
        return "qna/show";
    }

    @GetMapping("/questions/{id}/form")
    public String searchQuestion(@PathVariable Long id, HttpSession session, Model model) {
        if (!HttpSessionUtils.isLoginUser(session)) {
            return "redirect:/";
        }

        Question question = questionRepository.findOne(id);
        User user = HttpSessionUtils.getSessionedUser(session);
        if (!user.match(question.getWriter())) {
            return "redirect:/";
        }

        log.debug("Question {}", question);
        model.addAttribute("question", question);
        return "/qna/updateForm";
    }

    @PutMapping("/questions/{id}/form")
    public String updateQuestion(@PathVariable Long id, Question question, HttpSession session){
        if(!HttpSessionUtils.isLoginUser(session)){
            throw new IllegalStateException("You can't update, Please Login");
        }
        User user = HttpSessionUtils.getSessionedUser(session);
        if(!user.match(question.getWriter())){
            throw new IllegalStateException("You can't update another user's Question");
        }
        Question beforeQuestion = questionRepository.findOne(id);
        beforeQuestion.update(question);
        questionRepository.save(beforeQuestion);
        log.debug("Question {}", beforeQuestion);
        return "redirect:/";
    }

    @DeleteMapping("questions/{id}")
    public String delete(@PathVariable Long id, HttpSession session){
        if(!HttpSessionUtils.isLoginUser(session)){
            throw new IllegalStateException("You can't delete, Please Login");
        }
        User user = HttpSessionUtils.getSessionedUser(session);
        Question question = questionRepository.findOne(id);

        if(!user.match(question.getWriter())){
            throw new IllegalStateException("You can't delete another user's Question");
        }

        questionRepository.delete(id);
        log.debug("Question Delete");
        return "redirect:/";
    }
}
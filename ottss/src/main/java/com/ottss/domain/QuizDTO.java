package com.ottss.domain;

public class QuizDTO { // 퀴즈 DB
	private int quiz_num; // 퀴즈 번호. 시퀀스 아님
	private String quiz; // 문제
	private String answer; // 답안(선택지A)
	private String choice1; // 선택지B
	private String choice2; // 선택지C
	private String choice3; // 선택지D
	
	public int getQuiz_num() {
		return quiz_num;
	}
	public void setQuiz_num(int quiz_num) {
		this.quiz_num = quiz_num;
	}
	public String getQuiz() {
		return quiz;
	}
	public void setQuiz(String quiz) {
		this.quiz = quiz;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getChoice1() {
		return choice1;
	}
	public void setChoice1(String choice1) {
		this.choice1 = choice1;
	}
	public String getChoice2() {
		return choice2;
	}
	public void setChoice2(String choice2) {
		this.choice2 = choice2;
	}
	public String getChoice3() {
		return choice3;
	}
	public void setChoice3(String choice3) {
		this.choice3 = choice3;
	}
}

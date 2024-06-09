package QuizApplicationWithTimer;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

class Question {
    private String questionText;
    private String[] options;
    private int correctAnswer;

    public Question(String questionText, String[] options, int correctAnswer) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }
}

class Quiz {
    private Question[] questions;
    private int currentQuestionIndex;
    private int score;
    private Scanner scanner;
    private Timer timer;
    private boolean answered;
    private boolean timeUp;
    private int timeLimit;

    public Quiz(Question[] questions, int timeLimit) {
        this.questions = questions;
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.scanner = new Scanner(System.in);
        this.timeLimit = timeLimit;
    }

    public void start() {
        for (currentQuestionIndex = 0; currentQuestionIndex < questions.length; currentQuestionIndex++) {
            displayQuestion(questions[currentQuestionIndex]);
            startTimer();
            getAnswer();
            stopTimer();
        }
        displayResults();
    }

    private void displayQuestion(Question question) {
        System.out.println("\n" + question.getQuestionText());
        String[] options = question.getOptions();
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
    }

    private void getAnswer() {
        answered = false;
        timeUp = false;
        while (!answered && !timeUp) {
            System.out.print("Select an option (1-4): ");
            if (scanner.hasNextInt()) {
                int answer = scanner.nextInt();
                if (answer >= 1 && answer <= 4) {
                    checkAnswer(answer - 1);
                    answered = true;
                } else {
                    System.out.println("Invalid option. Please select between 1 and 4.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                scanner.next(); // Clear invalid input
            }
        }
    }

    private void checkAnswer(int answer) {
        Question question = questions[currentQuestionIndex];
        if (answer == question.getCorrectAnswer()) {
            score++;
        }
    }

    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!answered) {
                    System.out.println("\nTime's up!");
                    timeUp = true;
                    answered = true;  // Ensure the question loop exits
                }
            }
        }, timeLimit * 1000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void displayResults() {
        System.out.println("\nQuiz completed!");
        System.out.println("Your final score is: " + score + "/" + questions.length);
        for (int i = 0; i < questions.length; i++) {
            Question question = questions[i];
            System.out.println("\nQuestion " + (i + 1) + ": " + question.getQuestionText());
            System.out.println("Correct answer: " + question.getOptions()[question.getCorrectAnswer()]);
        }
    }

    public static void main(String[] args) {
        Question[] questions = new Question[] {
                new Question("What is the capital of France?", new String[] {"Berlin", "London", "Paris", "Madrid"}, 2),
                new Question("What is 2 + 2?", new String[] {"3", "4", "5", "6"}, 1),
                new Question("Which planet is known as the Red Planet?", new String[] {"Earth", "Mars", "Jupiter", "Saturn"}, 1),
                new Question("Who wrote 'To Kill a Mockingbird'?", new String[] {"Harper Lee", "Jane Austen", "Mark Twain", "Charles Dickens"}, 0)
        };
        Quiz quiz = new Quiz(questions, 10); // 10 seconds per question
        quiz.start();
    }
}

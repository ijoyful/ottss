const quiz_data = [
	{
	    "question": "Java 프로그램의 진입점(entry point)은?",
	    "answers": {
	      1: "public static void main(String args[])",
	      2: "System.out.println()",
	      3: "main()",
	      4: "class MyClass"
	    },
	    "correct": 1
	  },
	  {
	    "question": "Java는 어떤 프로그래밍 언어인가요?",
	    "answers": {
	      1: "절차적 프로그래밍 언어",
	      2: "객체지향 프로그래밍 언어",
	      3: "함수형 프로그래밍 언어",
	      4: "어셈블리 언어"
	    },
	    "correct": 2
	  },
	  {
	    "question": "다음 중 Java의 키워드가 아닌 것은?",
	    "answers": {
	      1: "synchronized",
	      2: "volatile",
	      3: "integer",
	      4: "transient"
	    },
	    "correct": 3
	  },
	  {
	    "question": "다음 중 Java의 데이터 타입이 아닌 것은?",
	    "answers": {
	      1: "int",
	      2: "boolean",
	      3: "double",
	      4: "real"
	    },
	    "correct": 4
	  },
	  {
	    "question": "Java의 기본 패키지는 무엇인가요?",
	    "answers": {
	      1: "java.lang",
	      2: "java.util",
	      3: "java.io",
	      4: "java.net"
	    },
	    "correct": 1
	  },
	  {
	    "question": "Java에서 배열의 인덱스는 몇부터 시작하나요?",
	    "answers": {
	      1: "1",
	      2: "-1",
	      3: "0",
	      4: "사용자가 설정"
	    },
	    "correct": 3
	  },
	  {
	    "question": "다음 중 생성자(Constructor)의 특징이 아닌 것은?",
	    "answers": {
	      1: "클래스 이름과 동일하다",
	      2: "반환 타입이 없다",
	      3: "오버로딩이 가능하다",
	      4: "반드시 명시적으로 정의해야 한다"
	    },
	    "correct": 4
	  },
	  {
	    "question": "String 객체는 변경할 수 없는 이유는?",
	    "answers": {
	      1: "String은 상속 불가능한 클래스다",
	      2: "String은 primitive 타입이다",
	      3: "String은 immutable 객체다",
	      4: "String은 스택 메모리에 저장된다"
	    },
	    "correct": 3
	  },
	  {
	    "question": "Java에서 break 키워드는 주로 어디에 사용되나요?",
	    "answers": {
	      1: "메서드 종료",
	      2: "반복문 종료",
	      3: "프로그램 종료",
	      4: "예외 처리"
	    },
	    "correct": 2
	  },
	  {
	    "question": "Java에서 다형성(polymorphism)이란?",
	    "answers": {
	      1: "한 클래스가 여러 메서드를 가질 수 있음",
	      2: "동일한 메서드가 다양한 객체에서 동작할 수 있음",
	      3: "여러 스레드를 동시에 실행할 수 있음",
	      4: "객체 간 데이터 공유를 가능하게 함"
	    },
	    "correct": 2
	  },
	  {
	    "question": "다음 코드의 출력 결과는?\n\nint x = 5;\nx += 3;\nSystem.out.println(x);",
	    "answers": {
	      1: "5",
	      2: "8",
	      3: "3",
	      4: "0"
	    },
	    "correct": 2
	  },
	  {
	    "question": "Java에서 finally 블록은 언제 실행되나요?",
	    "answers": {
	      1: "예외가 발생하지 않을 때만",
	      2: "예외가 발생할 때만",
	      3: "항상 실행된다",
	      4: "호출된 경우에만"
	    },
	    "correct": 3
	  },
	  {
	    "question": "다음 중 ArrayList에 대해 올바른 설명은?",
	    "answers": {
	      1: "크기가 고정되어 있다",
	      2: "동적 크기를 지원한다",
	      3: "원시 타입 데이터를 저장한다",
	      4: "정렬된 데이터를 저장한다"
	    },
	    "correct": 2
	  },
	  {
	    "question": "Java에서 Map 인터페이스의 주요 구현체는?",
	    "answers": {
	      1: "HashMap, TreeMap",
	      2: "ArrayList, LinkedList",
	      3: "Stack, Queue",
	      4: "HashSet, TreeSet"
	    },
	    "correct": 1
	  },
	  {
	    "question": "다음 코드에서 num의 값은?\n\nint num = Math.max(10, 20);",
	    "answers": {
	      1: "10",
	      2: "20",
	      3: "15",
	      4: "0"
	    },
	    "correct": 2
	  },
	  {
	    "question": "다음 코드의 출력 결과는?\n\nint[] arr = {1, 2, 3};\nSystem.out.println(arr.length);",
	    "answers": {
	      1: "3",
	      2: "2",
	      3: "1",
	      4: "오류 발생"
	    },
	    "correct": 1
	  },
	  {
	    "question": "다음 코드에서 예외를 발생시키는 문장은?\n\nString s = null;\nSystem.out.println(s.length());",
	    "answers": {
	      1: "예외 없음",
	      2: "NullPointerException",
	      3: "IndexOutOfBoundsException",
	      4: "ArithmeticException"
	    },
	    "correct": 2
	  },
	  {
	    "question": "Java의 interface와 abstract class의 차이점은?",
	    "answers": {
	      1: "인터페이스는 다중 상속이 가능하다",
	      2: "추상 클래스는 인스턴스를 생성할 수 있다",
	      3: "인터페이스는 구현된 메서드를 가질 수 없다 (Java 8 이전)",
	      4: "A와 C 모두"
	    },
	    "correct": 4
	  },
	  {
	    "question": "다음 코드의 출력 결과는?\n\nString s1 = \"Hello\";\nString s2 = \"Hello\";\nSystem.out.println(s1 == s2);",
	    "answers": {
	      1: "true",
	      2: "false",
	      3: "NullPointerException",
	      4: "컴파일 오류"
	    },
	    "correct": 1
	  },
	  {
	    "question": "다음 중 Java에서 쓰레드(Thread)를 생성하는 방법이 아닌 것은?",
	    "answers": {
	      1: "Thread 클래스를 상속받는다",
	      2: "Runnable 인터페이스를 구현한다",
	      3: "Callable 인터페이스를 구현한다",
	      4: "ActionListener 인터페이스를 구현한다"
	    },
	    "correct": 4
	  },
	  {
	    "question": "Java에서 synchronized 키워드는 무엇을 보장하나요?",
	    "answers": {
	      1: "성능 최적화",
	      2: "메서드와 블록의 스레드 안전성",
	      3: "예외 처리를 단순화",
	      4: "가비지 컬렉션 우선순위 설정"
	    },
	    "correct": 2
	  },
	  {
	    "question": "다음 코드에서 x의 값은?\n\nint x = 5;\nx = x++;",
	    "answers": {
	      1: "5",
	      2: "6",
	      3: "0",
	      4: "컴파일 오류"
	    },
	    "correct": 1
	  },
	  {
	    "question": "Java에서 final 키워드를 변수에 사용할 때의 의미는?",
	    "answers": {
	      1: "변수는 변경할 수 없다",
	      2: "변수는 상속될 수 없다",
	      3: "변수는 상속 가능하다",
	      4: "변수는 지역 변수가 될 수 없다"
	    },
	    "correct": 1
	  },
	  {
	    "question": "Java에서 JVM의 역할은?",
	    "answers": {
	      1: "소스 코드를 바이트코드로 변환한다",
	      2: "바이트코드를 실행한다",
	      3: "바이트코드를 기계어로 변환한다",
	      4: "B와 C"
	    },
	    "correct": 4
	  },
	  {
	    "question": "다음 중 Java의 JIT 컴파일러가 하는 역할은?",
	    "answers": {
	      1: "컴파일 시간 단축",
	      2: "실행 중인 코드의 성능 최적화",
	      3: "코드 디버깅",
	      4: "A와 B 모두"
	    },
	    "correct": 4
	  },
	  {
	    "question": "JSP 파일의 확장자는 무엇인가요?",
	    "answers": {
	      1: ".jsp",
	      2: ".html",
	      3: ".java",
	      4: ".xml"
	    },
	    "correct": 1
	  },
	  {
	    "question": "JSP 페이지에서 Java 코드를 삽입하려면 어떤 태그를 사용하나요?",
	    "answers": {
	      1: "<% ... %>",
	      2: "<script> ... </script>",
	      3: "<java> ... </java>",
	      4: "<%= ... %>"
	    },
	    "correct": 1
	  },
	  {
	    "question": "JSP에서 표현 언어(Expression Language)는 어떤 기호로 시작하나요?",
	    "answers": {
	      1: "$",
	      2: "#",
	      3: "%",
	      4: "&"
	    },
	    "correct": 1
	  },
	  {
	    "question": "JSP에서 클라이언트로 출력을 직접 전달하려면 어떤 태그를 사용하나요?",
	    "answers": {
	      1: "<%= ... %>",
	      2: "<% ... %>",
	      3: "<%@ ... %>",
	      4: "<%! ... %>"
	    },
	    "correct": 1
	  },
	  {
	    "question": "JSP에서 사용되는 기본 내장 객체가 아닌 것은?",
	    "answers": {
	      1: "request",
	      2: "response",
	      3: "input",
	      4: "session"
	    },
	    "correct": 3
	  },
	  {
	    "question": "JSP 페이지는 실행 중 어떤 언어로 변환되나요?",
	    "answers": {
	      1: "HTML",
	      2: "Java",
	      3: "JavaScript",
	      4: "XML"
	    },
	    "correct": 2
	  },
	  {
	    "question": "JSP의 주석 태그는 무엇인가요?",
	    "answers": {
	      1: "<%-- ... --%>",
	      2: "<!-- ... -->",
	      3: "// ...",
	      4: "/* ... */"
	    },
	    "correct": 1
	  },
	  {
	    "question": "JSP에서 include 디렉티브는 어떤 용도로 사용되나요?",
	    "answers": {
	      1: "다른 JSP 파일의 출력을 포함",
	      2: "Java 클래스 파일을 가져오기",
	      3: "HTML 코드를 삽입",
	      4: "다른 JSP 파일을 컴파일에 포함"
	    },
	    "correct": 4
	  },
	  {
	    "question": "다음 중 JSP 페이지에서 선언문에 사용하는 태그는?",
	    "answers": {
	      1: "<%! ... %>",
	      2: "<% ... %>",
	      3: "<%= ... %>",
	      4: "<%-- ... --%>"
	    },
	    "correct": 1
	  }
	];

	let cp = "/ottss";
	
// 문제
window.addEventListener('load', () => {
	const sliderWrapEl = document.querySelector('div.slider-wrap');
	
	//quiz_data 배열 무작위로 섞기
	const shuffleQuizData = quiz_data.sort(()=>Math.random() - 0.5).slice(0,30)
	shuffleQuizData.forEach((item, index) => {
		let htmlText = `<div class="item">`;
		htmlText += `<p data-num="${index}">${index+1}) ${item.question}</p>`;
		htmlText += `<ol>`;
		for(let no in item.answers) {
			htmlText += `<li><label for="a${index}-${no}"> <input type="radio" value="${no}" name="question${index}" id="a${index}-${no}"> ${item.answers[no]} </label></li>`;
		}
		htmlText += `</ol>`;
		htmlText += `</div>`;
		
		sliderWrapEl.insertAdjacentHTML('beforeend', htmlText);
	});
});

// 슬라이더
const btnStartEl = document.querySelector('.btn-start');

btnStartEl.addEventListener('click', () => {
	if(! confirm('퀴즈를 시작하시겠습니까? 10포인트가 소모됩니다. ')) {
				return;
			}
	const sliderWrapEl = document.querySelector('div.slider-wrap');
	const itemListsEls = document.querySelectorAll('.slider-content > .slider-wrap > .item');
	if(itemListsEls.length === 0) {
		return false;
	}
	
	const btnLeftEl = document.querySelector('.slider-left .btn-move-left');
	const btnRightEl = document.querySelector('.slider-right .btn-move-right');

	const itemWidth = itemListsEls[0].clientWidth;
	let currentIndex = 0;
	let positionValue = 0;
	
	const itemPrevious = () => {
		 if(currentIndex > 0) {
			positionValue += itemWidth;
			sliderWrapEl.style.transform = `translateX(${positionValue}px)`;
			currentIndex--;
			return;
		 }
		 
		 alert('처음 문제 입니다.');
	};

	const itemNext = () => {
		 if(currentIndex < itemListsEls.length - 1) {
			positionValue -= itemWidth;
			sliderWrapEl.style.transform = `translateX(${positionValue}px)`;
			currentIndex++;
			return;
		 }
		 
		 alert('마지막 문제 입니다.');
	};
	
	btnLeftEl.addEventListener('click', () => {
		itemPrevious();
	});

	btnRightEl.addEventListener('click', () => {
		itemNext();
	});
});


window.addEventListener('load', () => {
	const btnSubmitEL = document.querySelector("button.btnSubmit");
	const timerEL = document.querySelector("div.timer");
	
	let timeLimit = 120;  // 제한시간(2분)
	
	// 타이머
	const quizRemaining = () => {
		if( timeLimit < 0 ) {
			clearInterval(timer);
			btnSubmitEL.setAttribute('disabled', 'true');
			check();
		} else {
			let minute = Math.floor(timeLimit / 60);
			minute = minute < 10 ? '0' + minute : minute;
			let second = Math.floor(timeLimit % 60);
			second = second < 10 ? '0' + second : second;
			
			timerEL.innerHTML = `${minute}:${second}`;
			
			timeLimit--;   
		}
	};
	quizRemaining();
	const timer = setInterval(quizRemaining, 1000);
	
	// 채점
	const check = () => {
		const containerEl = document.querySelector('div.slider-container');
		const questionEls = document.querySelectorAll('.slider-wrap > div.item');
		const btnSubmitEL = document.querySelector("button.btnSubmit");
		
		containerEl.style.display = 'block';
		btnSubmitEL.style.display = 'none';
		
		let htmlText = '';
		let count  = 0;
		let bResult;
		for(let el of questionEls) {
			let num = parseInt(el.querySelector('p').dataset.num);
			let correct = quiz_data[num].correct;
			const radioEL = el.querySelector('input[type=radio]:checked');
			let select = radioEL ? parseInt(radioEL.value) : 0;
			bResult = false;
			if(correct === select) {
				count++;
				bResult = true;
			}
			
			if(select === 0) select = '선택안함';
			
			// htmlText += `<p class="scoring-result">${num+1}번 정답 : ${correct} → ${select}, ${bResult ? "○":"×"}</p>`;
		}
		htmlText += `<p class="scoring-result"> 정답 : ${count}/30개</p>`;
		htmlText += `<p class="scoring-result"> 획득 포인트 : ${count*2}점</p>`;
		htmlText += `<div class="returnBtnWarp"><a href="${cp+'/games/quiz'}" class='returnBtn'>처음으로</a></div>`;
		
		containerEl.innerHTML = htmlText;
	};
	
	btnSubmitEL.addEventListener('click', () => {
		if(! confirm('제출하시겠습니까 ? ')) {
			return;
		}
		
		check();
		clearInterval(timer);
	});
	
});

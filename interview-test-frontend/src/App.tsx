import { useState } from 'react'
import axios from 'axios'
import './App.css'

interface QuestionDTO {
  question: string;
  options: string[];
  correctAnswer: string;
  explanation: string;
}

interface TestResponse {
  testId: string;
  questions: QuestionDTO[];
}

enum TestCategory {
  CORE_JAVA = 'CORE_JAVA',
  SPRING_FRAMEWORK = 'SPRING_FRAMEWORK',
  SQL = 'SQL',
  DESIGN_PATTERNS = 'DESIGN_PATTERNS'
}

const categoryDisplayNames = {
  [TestCategory.CORE_JAVA]: 'Core Java',
  [TestCategory.SPRING_FRAMEWORK]: 'Spring Framework',
  [TestCategory.SQL]: 'SQL',
  [TestCategory.DESIGN_PATTERNS]: 'Design Patterns'
};

function App() {
  const [category, setCategory] = useState<TestCategory | ''>('')
  const [questions, setQuestions] = useState<QuestionDTO[]>([])
  const [results, setResults] = useState(null)
  const [testId, setTestId] = useState('')
  const [answers, setAnswers] = useState<string[]>([])

  const categories = Object.values(TestCategory)

  const startTest = async () => {
    try {
      const response = await axios.get<TestResponse>(`/api/test/start/${category}`, {
        withCredentials: true
      });

      console.log('Test başlatıldı:', response.data);

      if (!response.data.testId || !response.data.questions) {
        throw new Error('Sunucudan geçersiz yanıt alındı');
      }

      setQuestions(response.data.questions);
      setTestId(response.data.testId);
      setAnswers(new Array(response.data.questions.length).fill(''));
    } catch (error) {
      console.error('Error fetching questions:', error);
      if (axios.isAxiosError(error) && error.response) {
        const errorMessage = error.response.data.message || 'Bilinmeyen bir hata oluştu';
        const errorDetails = error.response.data.details || '';
        alert(`Hata: ${errorMessage}\n${errorDetails}`);
      } else {
        alert('Sorular yüklenirken bir hata oluştu');
      }
    }
  };

  const submitResults = async () => {
    if (!testId || !answers || answers.length === 0) {
      alert('Lütfen tüm soruları cevaplayın');
      return;
    }

    console.log('Gönderilen veriler:', {
      testId: testId,
      category: category,
      answers: answers,
      weakTopics: []
    });

    try {
      const response = await axios.post('/api/test/submit', {
        testId: testId,
        category: category,
        answers: answers,
        weakTopics: []
      }, {
        withCredentials: true
      });
      
      console.log('Backend yanıtı:', response.data);
      setResults(response.data);
    } catch (error) {
      console.error('Submit hatası:', error);
      if (axios.isAxiosError(error) && error.response) {
        const errorMessage = error.response.data.message || 'Bilinmeyen bir hata oluştu';
        const errorDetails = error.response.data.details || '';
        alert(`Hata: ${errorMessage}\n${errorDetails}`);
      } else {
        alert('Sonuçlar gönderilirken bir hata oluştu');
      }
    }
  };

  return (
    <div className="App">
      <h1>Java Interview Practice Platform</h1>
      <div>
        <label>Select Category:</label>
        <select value={category} onChange={(e) => setCategory(e.target.value as TestCategory)}>
          <option value="">--Select--</option>
          {categories.map((cat) => (
            <option key={cat} value={cat}>
              {categoryDisplayNames[cat]}
            </option>
          ))}
        </select>
        <button onClick={startTest} disabled={!category}>
          Start Test
        </button>
      </div>
      <div>
        {questions.length > 0 && (
          <div>
            <ul>
              {questions.map((question, index) => (
                <li key={index}>
                  <p>{question.question}</p>
                  <select 
                    value={answers[index]} 
                    onChange={(e) => {
                      const newAnswers = [...answers]
                      newAnswers[index] = e.target.value
                      setAnswers(newAnswers)
                    }}
                  >
                    <option value="">--Seçiniz--</option>
                    {question.options.map((option, optIndex) => (
                      <option key={optIndex} value={option}>
                        {option}
                      </option>
                    ))}
                  </select>
                </li>
              ))}
            </ul>
            <button onClick={submitResults}>Submit Results</button>
          </div>
        )}
        {results && <div>Test Results: {JSON.stringify(results)}</div>}
      </div>
    </div>
  )
}

export default App

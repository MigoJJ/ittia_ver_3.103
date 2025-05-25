package je.pense.doro.soap.plan;

public class ittiaGDSPlanPanel_2_String {
	    private String retA;
	    private String[] retB;

	    public ittiaGDSPlanPanel_2_String(String retA, String[] retB) {
	        this.retA = retA;
	        this.retB = retB;
	    }

	    public String getRetA() {
	        return retA;
	    }

	    public String[] getRetB() {
	        return retB;
	    }
	    
	    public static ittiaGDSPlanPanel_2_String myMethod(int i) {
	        String retA = "";
	        String[] retB ={"",""};
	        switch (i) {
		        case 1:
		        	retA = "Diabetes Mellitus";
		            retB = new String[]{
		            		"Your blood sugar control is the foundation of managing your diabetes.",
		            		"Medication is just one part of the picture. Diet and exercise are equally crucial.",
		            		"Regular foot checks are very important to prevent diabetic foot complications.",
		            		"It's important to have your eyes checked regularly by an ophthalmologist.",
		            		"Keep up with your scheduled blood tests, like the A1c.",
		            		"Be aware of the symptoms of both high and low blood sugar.",
		            		"Managing diabetes can be challenging, but remember that you are not alone.",
		            		"It's very important that you follow your medication schedule.",
		            		"Make sure you have a diabetes management plan ready before traveling or during emergencies.",
		            		"Let's work together as a team. Your active participation in managing your diabetes will lead to a healthier and longer life."
		            };
		            break;
		        case 2:
		        	retA = "Hypertension";
		            retB = new String[]{
		            		"Managing your blood pressure is crucial for protecting your heart, brain, and kidneys.",
		            		"Lifestyle changes are a cornerstone of hypertension management.",
		            		"It's essential that you take your blood pressure medication exactly as prescribed.",
		            		"Monitoring your blood pressure at home can help us track how well your treatment is working.",
		            		"High blood pressure often has no symptoms, so regular monitoring is the only way to know if your blood pressure is in a healthy range.",
		            		"If you smoke, quitting is the single best thing you can do for your blood pressure and overall health.",
		            		"If you are overweight or obese, losing even a small amount of weight can significantly lower your blood pressure.",
		            		"Be mindful of stress and learn healthy ways to manage it, as stress can sometimes raise your blood pressure.",
		            		"It's very important to avoid excess alcohol consumption as this can raise your blood pressure and interfere with your medications.",
		            		"High blood pressure can increase your risk of heart attack, stroke, and kidney disease, but with effective management, we can help prevent these complications."
		                    };		            
	            	break;
		        case 3:
		        	retA = "Hypercholesterolemia";
		            retB = new String[]{
		            		"Lowering your cholesterol is vital for protecting your heart and blood vessels.",
		            		"Diet plays a significant role in managing cholesterol.",
		            		"Regular physical activity is also very important for lowering your cholesterol levels.",
		            		"If lifestyle changes alone aren't enough, we may need to consider medication to help lower your cholesterol.",
		            		"It’s essential to understand your LDL (bad) cholesterol, HDL (good) cholesterol, and triglyceride levels.",
		            		"Maintaining a healthy weight can significantly impact your cholesterol levels.",
		            		"Smoking is a major risk factor for heart disease. Quitting smoking is one of the best things you can do for your heart and overall health.",
		            		"We need to regularly monitor your cholesterol levels with blood tests.",
		            		"It’s essential to communicate any concerns or side effects that you might experience with your medications.",
		            		"Managing high cholesterol is a long-term commitment. Working together, we can reduce your risk of heart disease and improve your overall well-being."
		            };		            
	            	break;
		        case 4:
		        	retA = "Osteoporosis";
		            retB = new String[]{
		            		"Osteoporosis is a condition that weakens your bones, making them more prone to fractures.",
		            		"Lifestyle changes are crucial for managing osteoporosis.",
		            		"We need to monitor your bone density with regular DEXA scans.",
		            		"If lifestyle changes aren't enough, we may need to consider medication to help slow down bone loss and reduce your fracture risk.",
		            		"It’s important to be aware of your risk of falls, and we should take steps to prevent them.",
		            		"Be sure to get enough calcium and vitamin D in your diet.",
		            		"Weight-bearing exercises, such as walking, jogging, or dancing, are beneficial for strengthening your bones.",
		            		"It is important to follow your treatment plan as outlined.",
		            		"Some medications can affect bone health.",
		            		"Managing osteoporosis is a long-term commitment. With the right approach, we can help keep your bones strong and reduce your risk of fractures."
		            };		            
	            	break;
		        case 5:
		        	retA = "URI";
		            retB = new String[]{
		            		"It seems like you have a typical upper respiratory infection, which is usually caused by a virus.",
		            		"Rest is crucial for your body to recover.",
		            		"Staying hydrated is very important.",
		            		"Over-the-counter medications can help relieve your symptoms.",
		            		"A humidifier or steam can help soothe your airways and loosen congestion.",
		            		"Gargling with warm salt water can help soothe a sore throat.",
		            		"It's important to cover your mouth and nose when you cough or sneeze and to wash your hands frequently.",
		            		"Be aware of worsening symptoms or new symptoms such as high fever, severe chest pain, difficulty breathing, or persistent cough that gets worse over time.",
		            		"Most URIs resolve within a week to ten days. If your symptoms do not improve or get worse after this time, please return to see me.",
		            		"It's important to let your body heal. Focus on good nutrition and get adequate rest."
		            };		            
	            	break;
		        case 6:
		        	retA = "Thyroid function test F/U";
		            retB = new String[]{
		            		"Plan to TSH, free thyroxine (T4), T3\n",
				           "Plan to TSH, free thyroxine (T4), T3 + Autoantibodies\n",
				           "Plan to TSH, free thyroxine (T4), T3 + Autoantibodies\n"
				           + "\tp.r.n.> TUS"
		            };		            
	            	break;

		        case 7:
		        	retA = "Obtain Diabetes F/U";
		            retB = new String[]{
		            		"Obtain to FPG, HbA1C, Lipid profile\n"
				            		 + "\tserum creatinine, eGFR, +A/C ratio)\n"
				            		 + "\tLFT, Electrolyte panel, CBC), Lp(a), ApoB\n"
				            		 + "\tVitamin D level",
				            		
		            		
		            		"Obtain to FPG, HbA1C, Lipid profile\n"
		            		 + "\tKidney function tests (serum creatinine\n"
		            		 + "    estimated glomerular filtration rate (eGFR)\n"
		            		 + "    urine albumin-to-creatinine ratio)\n"
		            		 + "\tLiver function tests\n"
		            		 + "\tElectrolyte panel\n"
		            		 + "\tThyroid function tests\n"
		            		 + "\tComplete blood count (CBC)\n"
		            		 + "\tVitamin D level",
		            		 
		            		"Obtain to Fasting plasma glucose (FPG)\n"
				            	+ "\tGlycated hemoglobin (HbA1C)\n",
				            		 
				            "Obtain to Lipid profile (total, LDL, HDL, triglycerides)\n",
		            };	
		            break;
		            
		        case 8:
		        	retA = "Genetal";
		            retB = new String[]{
		            		"오늘 와주셔서 감사합니다. 건강에 대한 걱정거리가 있으실 때 빨리 진료를 받으시는 것이 중요합니다.",
				            "궁금한 점이 있으시면 언제든지 편하게 질문해주세요. 무슨 일이 있는지, 그리고 우리의 계획이 무엇인지 이해하시는 것이 중요합니다. 질문은 언제나 환영입니다.",
				            "건강은 팀워크입니다. 저는 당신을 안내하고 지원하기 위해 여기 있지만, 당신이 건강을 관리하는 데 적극적으로 참여하는 것이 최선의 결과를 위해 매우 중요합니다.",
				            "정확한 건강 상태를 파악하는 것이 저에게 매우 중요합니다. 증상, 병력, 그리고 복용하시는 약이나 건강보조제에 대해 솔직하게 말씀해주세요.",
				            "균형 잡힌 식단과 규칙적인 운동과 같은 생활 습관 변화는 건강에 큰 영향을 미칠 수 있습니다. 함께 이러한 변화를 어떻게 적용하고, 당신에게 맞춤화된 계획을 세울 수 있을지 논의해 봅시다.",
				            "치료 계획을 잘 따르는 것이 매우 중요합니다. 약을 처방대로 복용하거나 지시사항을 따르는 것이 중요합니다. 어려움이 있으시면 언제든지 알려주세요.",
				            "예약된 모든 후속 진료를 꼭 받으시기 바랍니다. 이를 통해 진행 상황을 모니터링하고, 필요한 치료 계획을 조정하고, 문제를 조기에 발견할 수 있습니다.",
				            "당신의 건강을 스스로 옹호하십시오. 무언가 잘못되었다고 느껴지면 무시하지 마십시오. 항상 저나 다른 의료 전문가와 소통하십시오.",
				            "건강 문제를 다루는 것이 어려울 수 있다는 것을 이해합니다. 혼자가 아니라는 것을 알아주세요. 저는 이 과정을 통해 당신의 이야기를 듣고 당신을 지원하기 위해 여기에 있습니다.",
				            "오늘 우리 계획과 앞으로 무엇을 기대해야 할지에 대해 명확히 이해하시고 가셨으면 합니다. 떠나시기 전에 당신의 모든 질문과 우려 사항을 해결했는지 확인해 봅시다."
		            };			            
		            
		        default:
		            System.out.println("ReEnter the Number !!!");
		            break;
		    }
	        return new ittiaGDSPlanPanel_2_String(retA, retB);
	    }
	    
}

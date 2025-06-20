package com.ssginc8.docto.chatbot.classifier;

import static com.ssginc8.docto.doctor.entity.Specialization.*;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import com.ssginc8.docto.doctor.entity.Specialization;


@Component
public class SymptomClassifier {

	private static final List<Pair<Pattern, Specialization>> RULES = List.of(
		// 내과 (INTERNAL_MEDICINE)
		Pair.of(Pattern.compile(
			"기침|가래|열|오한|흉통|소화|속쓰림|설사|변비|피로|체한|복통"
		), INTERNAL_MEDICINE),

		// 심장내과 (CARDIOLOGY)
		Pair.of(Pattern.compile(
			"두근거림|부정맥|심장|가슴통증|협심증|심근|호흡곤란|심계|가슴답답|심전도"
		), CARDIOLOGY),

		// 신경과 (NEUROLOGY)
		Pair.of(Pattern.compile(
			"두통|편두통|어지럼|뇌졸중|경련|발작|손발저림|말·언어장애|파킨슨|치매|깜박|건망증"
		), NEUROLOGY),

		// 피부과 (DERMATOLOGY) *
		Pair.of(Pattern.compile(
			"가려움|두드러기|습진|여드름|뾰루지|피부트러블|탈모|발진|건선|아토피"
		), DERMATOLOGY),

		// 영상의학과 (RADIOLOGY)
		Pair.of(Pattern.compile(
			"엑스레이|MRI|CT|초음파|조영제|사진촬영|영상검사|방사선|골밀도|혈관조영"
		), RADIOLOGY),

		// 종양내과 (ONCOLOGY)
		Pair.of(Pattern.compile(
			"암|종양|항암|림프종|전이|종괴|종양표지자|조기검진|이차암|방사선치료|몽우리|볼록"
		), ONCOLOGY),

		// 산부인과 (GYNECOLOGY)
		Pair.of(Pattern.compile(
			"질염|자궁|난소|갱년기|유방통|간지러움"
		), GYNECOLOGY),

		// 정신과 (PSYCHIATRY)
		Pair.of(Pattern.compile(
			"우울|불안|공황|스트레스|불면|공포증|강박|트라우마|틱장애|ADHD"
		), PSYCHIATRY),

		// 일반외과 / 정형외과 대용 (GENERAL_SURGERY)
		Pair.of(Pattern.compile(
			"무릎|허리|어깨|관절|팔꿈치|발목|손목|척추|뼈|인대"
		), GENERAL_SURGERY),

		// 비뇨기과 (UROLOGY)
		Pair.of(Pattern.compile(
			"소변|배뇨|방광|신장결석|전립선|혈뇨|요실금|야뇨|잔뇨감|요도통"
		), UROLOGY),

		// 안과 (OPHTHALMOLOGY)
		Pair.of(Pattern.compile(
			"시력|눈통증|안구건조|충혈|눈물|눈부심|다래끼|눈가려움|눈곱|흐릿"
		), OPHTHALMOLOGY),

		// 이비인후과 (ENT)
		Pair.of(Pattern.compile(
			"콧물|코막힘|비염|목아픔|인후통|귀통증|이명|중이염|어지러움|코피"
		), ENT)
	);


	// 매칭 결과 없으면 후속 질문으로 넘어감
	public java.util.Optional<Specialization> classifyOpt(String sentence) {
		return RULES.stream()
			.filter(p -> p.getLeft().matcher(sentence).find())
			.map(org.apache.commons.lang3.tuple.Pair::getRight)
			.findFirst();
	}
}

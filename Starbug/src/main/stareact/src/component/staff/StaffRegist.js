import axios from 'axios';

import React, { useEffect, useState } from "react";
import Address from './Address';

import style from './StaffRegist.css';

export default function StaffRegist(props){

// 인풋 박스 최대 글자수 이상 입력 불가
function maxlength(object){
    if (object.value.length > object.maxLength){
      object.value = object.value.slice(0, object.maxLength);
  }
}


    // 안사 등록 함수
    const onRegist = (e) =>{
    // form 호출
    let staffForm = document.querySelectorAll('.staffForm')[0];
    // formData 변환
    let staffFormData = new FormData(staffForm);


    axios
        .post("/staff/staffPost" , staffFormData)
        .then(r=>{
            if(r){
                alert('등록 완료');
                window.location.href="/staff?headerCategory=2";
            }else{
                alert('등록 실패')
            }
        })
    } //onRegist end



const [enroll_company, setEnroll_company] = useState({
	address:'',
});

const [popup, setPopup] = useState(false);

const handleInput = (e) => {
	setEnroll_company({
    	...enroll_company,
        [e.target.name]:e.target.value,
    })
}

const handleComplete = (data) => {
    setPopup(!popup);
}



    return(<>

    <div className="swrap">
        <div className="staffWrap">
             <form className="staffForm">

             <div className="sTop">

                {/* 첫번재 구역*/}

                <div className="splace1">
                    <div>이름</div>
                    <div>주민번호</div>
                    <div>연락처</div>
                    <div>결혼</div>
                    <div>생일</div>
                </div>

                {/*두번째 구역 */}
                <div className="splace2">
                <input className="sname" type="text" name="sname" placeholder="이름"/>
                <div className="sidnumCss">
                <input className="sidnum1" type="text" name="sidnum1" placeholder="생년월일" maxlength={6}/>-
                <input className="sidnum2" type="text" name="sidnum2" placeholder="뒷자리"maxlength={7}/>
                </div>
                <input className="sphone" type="text" name="sphone" maxlength={13} placeholder="010-0000-0000" />
                    <div className="sRadio">
                        <input type="radio" className="smarry" name="smarry" value="기혼"/>기혼
                        <input type="radio" className="smarry" name="smarry" value="미혼"/>미혼
                    </div>
                    <div className="sRadio">
                        <input type="radio" className="ssorl" name="ssorl" value="양력"/>양력
                        <input type="radio" className="ssorl" name="ssorl" value="음력"/>음력
                    </div>
                </div>


                {/* 세번째 구역 */}
                <div className="splace3">
                    <div>직급</div>
                    <div>이메일</div>
                    <div>비상연락처</div>
                    <div>생일</div>
                    <div>입사일</div>
                </div>


                {/* 네번째 구역*/}

                <div className="splace4">
                   <select className="sposition" name="sposition">
                        <option value="사원">사원</option>
                        <option value="대리">대리</option>
                        <option value="과장">과장</option>
                        <option value="점장">점장</option>
                   </select>
                   <input className="semail" type="email" name="semail" placeholder="email"/>
                   <input className="sephone" type="text" name="sephone" maxlength={13} placeholder="010-0000-0000" />
                   <div className="sRadio">
                   <input type="radio" className="ssex" name="ssex" value="남성"/>남성
                   <input type="radio" className="ssex" name="ssex" value="여성"/>여성
                   </div>
                   <input className="sjoined" type="date" name="sjoined"/>
                </div>
             </div>

                {/* 주소 출력 구역 */}

        <div className="sBottom">
                <div className="splace1">
                    <div>주소</div>
                    <div>상세주소</div>
                    <div>사진</div>
                </div>

                <div className="saplace">
                    <input className="user_enroll_text" type="text" required={true} name="saddress" onChange={handleInput} value={enroll_company.address} placeholder="주소를 검색해주세요"/>
                    <button onClick={handleComplete}>주소 검색</button>
                    <div>
                        <input className="sdaddress" name="sdaddress" type="text" placeholder="상세주소" />
                        <input className="sdaddress" type="file" name="file" placeholder="사진을 등록하세요" multiple />
                        <button onClick={onRegist} type="button"> 등록 </button>
                        {popup && <Address company={enroll_company} setcompany={setEnroll_company}></Address>}
                    </div>
                </div>

        </div>


            </form>
		</div>
    </div>
    </>)

}



/*

    <div className="simgPalce">
                이미지구역
    </div>


*/
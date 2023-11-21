import styles from './ResouresUpdate.css';
import { useState , useEffect } from 'react'
import axios from 'axios';

export default function ResouresUpdate(props){

    const [ resouresExist ,setResouresExist] = useState(false);

    const [ onSerach , setOnSerach] = useState({
        resno : 0
    });

    const exist = (e) =>{console.log(e.target.value)
        axios.get('/inventory/exist',{params : {resname:e.target.value}})
        .then(r=>{
        if(r.data!=null) {console.log(r.data);setResouresExist(r.data)}
        else {alert('검색한 자료가없습니다.')}
        })
    }

    const getResoures = (e) => {
        let resname=document.querySelector('.resname').value;
        console.log(resname)
         if (!resname.trim()) {
                alert('재료이름을 입력해주세요.'); // resname이 비어 있거나 공백만 포함된 경우 알림 표시
                return;
         }
        axios.get('/inventory/serach',{params:{resname:resname}})
        .then ( (r)=>{ console.log(r.data)
        if(r.data!=""){setOnSerach(r.data);}
        else{alert("맞는 재료가없습니다.")}

            }

        )
    }

    const rescouresUpdate = (e) => {
       let form = document.querySelectorAll('.form')[0];
       console.log(form)
       let ResouresFormData = new FormData(form);
       console.log(ResouresFormData)

       // 유효성 검사
        if (ResouresFormData.get('resprice').trim() === ''&& ResouresFormData.get('resname').trim() === '') { // 두개의 값이 없을때
                alert('값을 입력해주세요.');
                return;
              }
       if (ResouresFormData.get('resname').trim() === '') {             //재료 이름이 없을때
         alert('재료 이름을 입력해주세요.');
         return;
       }

       if (ResouresFormData.get('resprice').trim() === '') {            //재료 금액이없을때
         alert('재료 금액을 입력해주세요.');
         return;
       }

     axios.get('/inventory/exist', { params: { resname: ResouresFormData.get('resname') } })
         .then(r => {
           setResouresExist(r.data);
           // 서버에 수정 요청
           axios.put('/inventory/resouresupdate', ResouresFormData)
             .then((response) => {
               console.log(response.data);
               if (response.data) {
                 alert("수정 성공");
                 window.location.href = '/inventory/category?headerCategory=5';
               } else {
                 alert("수정 오류.");
               }
             })
             .catch((error) => {
               console.error("수정 요청 에러:", error);
               alert("수정 중 에러가 발생했습니다.");
             });
         })
         .catch((error) => {
           console.error("exist 검사 요청 에러:", error);
           alert("exist 검사 중 에러가 발생했습니다.");
         });
     };



    return(<>
        <div className="updateWrap">
                <div className="updateTop">
                    <div className="pageComments">재고(재료)수정</div>
                    <div className="resouresName">Resoures Name</div>
                    <input className="resname" onChange={exist} type = "text" maxlength ="8" placeholder="재료이름을 입력해주세요"/>

                      {
                           resouresExist ? <p>검색한재료가있습니다.</p>:<p>검색한재료가없습니다</p>
                       }
                         <button onClick={getResoures} type="button" >검색 </button>

                </div>

                {onSerach.resno == 0 ? (
                  <div className="updateContent">
                    <div className="updateContentOne"><p>재료 번호</p><input className="inputBox" type="text" placeholder="재료가없습니다." readOnly={true} /></div>
                     <div className="updateContentTwo"><p>카테고리</p><  input className="inputBox"  type="text" className="inputBox" placeholder="재료가없습니다." readOnly={true} /></div>
                    <div className="updateContentThree"><p>재료 이름</p><input className="inputBox"  type="text" placeholder="재료가없습니다." readOnly={true} /></div>
                    <div className="updateContentFour"><p>재료 금액</p>< input className="inputBox"  type="text"  className="inputBox" placeholder="재료가없습니다." readOnly={true} /></div>

                  </div>
                ) : (
                  <div className="updateContent">
                    <form className="form">
                      <div className="updateContentOne" ><p>재료 번호</p><input className="inputBox"  name="resno" type="text" value={onSerach.resno} readOnly={true} /></div>
                      <div className="updateContentTwo"><p>카테고리</p><input className="inputBox"  name="rescname"type="text" value={onSerach.rescname} readOnly={true}/></div>
                      <div className="updateContentThree"><p>재료 이름</p><input className="inputBox"
                       maxlength ="8" name="resname" type="text" placeholder={onSerach.resname} /></div>
                      <div className="updateContentFour"><p>재료 금액</p><input className="inputBox"
                      maxlength ="6" name="resprice" type="text" placeholder={onSerach.resprice} /></div>
                      <button className="serachResoures" onClick={rescouresUpdate} type="button">수정하기</button>

                    </form>

                  </div>
                )}
             <div>

             </div>


        </div>

    </>)
}
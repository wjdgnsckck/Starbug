import axios from 'axios';
import {useState , useEffect} from 'react';
import style from './StaffInfo.css';


export default function SalaryUpdate (props){
    let slno = props.item;
    console.log(slno);

    // 가져온 급여 정보 담는다
    const [salaryInfo , setSalaryInfo ] = useState({ })

    useEffect( ()=> {salaryListGet()} , [])

    // slno 가지고 급여정보 불러오기
    const salaryListGet = (e)=>{

        axios
            .get("/staff/salaryGetInfo" , {params : {slno}})
            .then(r=>{console.log(r.data)
                // 데이터 useState 저장
                setSalaryInfo(r.data)
            })
    }

    // 급여 정보 수정
    const salaryUpdate = (e)=>{

        const salalyForm = document.querySelectorAll('.salaryForm2')[0];
        const salalyFormData = new FormData(salalyForm);
        salalyFormData.set('slno' , slno);
        console.log(salalyFormData)


        axios
            .put("/staff/salaryPut" , salalyFormData , { headers: { "Content-Type": 'multipart/form-data'}} )
            .then(r=>{console.log(r)
                if(r.data){alert('수정 완료'); window.location.href="/staff?headerCategory=2"; }
                else{alert('수정실패')}
            })



    }

    // 급여 정보 삭제
    const salaryDelete = (e) =>{
        let infoDelete = prompt('삭제 하려면 "급여정보삭제" 를 입력 하세요')
        if(infoDelete=='급여정보삭제'){
            axios
                .delete("/staff/salaryDelete" , {params : {slno} })
                .then(r=>{
                    if(r){ alert('정보 삭제 완료')
                        window.location.href='/staff';
                    }
                    else{alert('삭제 실패')}
                })
        }else{alert('잘못 입력하셨습니다')}
    }
return(<>
        <form className="salaryForm2" >
            <div className="salaryInput">
            <div>기본급</div>
            <input value={salaryInfo.sbasepay} type="text" className="sbasepay" name="sbasepay"  onChange={ (e)=>{ setSalaryInfo( {...salaryInfo , sbasepay : e.target.value }) }}/>

            <div>장려금</div>
            <input value={salaryInfo.sincentive} type="text" className="sincentive" name="sincentive" onChange={ (e)=>{ setSalaryInfo( {...salaryInfo , sincentive : e.target.value }) }}/>

            <div>차감</div>
            <input value={salaryInfo.sdeductible}type="text" className="sdeductible" name="sdeductible" onChange={ (e)=>{ setSalaryInfo( {...salaryInfo , sdeductible : e.target.value }) }}/>
            <div>지급일</div>
            <input value={salaryInfo.sdate} type="date" className="sdate" name="sdate" onChange={ (e)=>{ setSalaryInfo( {...salaryInfo , sdate : e.target.value }) }}/>
            <button onClick={salaryUpdate} type="button" className="serchBtn"> 수정 </button>
            <button onClick={salaryDelete} type="button" className="serchBtn"> 삭제 </button>
            </div>
        </form>

</>)

}


/*


        axios
            .put("/staff/salaryPut" , salalyFormData)
            .then(r=>{console.log(r.data);
                if(r.data){alert('수정 성공'); }
                else{alert('수정 실패')}
            })


            */
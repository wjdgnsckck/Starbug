import axios from 'axios';
import {useState , useEffect} from 'react';
import style from './StaffInfo.css';

export default function TaaUpdate (props){

    let tno = props.item;
    console.log('props 넘어온 tno' + tno);

    const [taaInfo , setTaaInfo] = useState({ })

    useEffect( () => {taaListGet()}, [] )

    // 개별 급여 정보 호출
    const taaListGet = (e) =>{
        axios
            .get("/staff/taaGetInfo" , {params : {tno}})
            .then(r=>{
                setTaaInfo(r.data)
            })

    }

    // 근태 수정
    const taaPut = (e) => {


        const taaForm = document.querySelectorAll('.taaForm')[0];
        console.log(taaForm)
        const taaFormData = new FormData(taaForm)
        taaFormData.set('tno' , tno)

        axios
            .put("/staff/taaPut" , taaFormData, { headers: { "Content-Type": 'multipart/form-data'}})
            .then(r=>{
                if(r.data){alert('수정완료')}
                else{alert('수정 실패')}
            })

    }

        //근태 삭제
    const taaDelete = (e)=>{


        let taaDelete = prompt('삭제 하려면 "근태정보삭제" 를 입력 하세요')
            if(taaDelete == '근태정보삭제'){
               axios
                   .delete("/staff/taaDelete" , {params : {tno}} )
                   .then(r=>{
                        if(r){
                            alert('삭제 완료')
                            window.location.href='/staff';}
                        else{alert('삭제 실패')}

                   })
            }else{alert('잘못 입력하셨습니다')}

    }



return(<>
        <form className="taaForm">
        <div className="salaryTop">
            미준수
            <select className="tstate" name="tstate" value={taaInfo.tstate} onChange={ (e)=>{ setTaaInfo( {...taaInfo , tstate : e.target.value }) }}>
                <option value="지각" >지각</option>
                <option value="결근" >결근</option>
            </select>
            발생일
            <input type="date" className="tdate" name="tdate" value={taaInfo.tdate} onChange={ (e)=>{ setTaaInfo( {...taaInfo , tdate : e.target.value }) }} />
            <button onClick={taaPut} type="button" className="serchBtn"> 수정 </button>
            <button onClick={taaDelete} type="button" className="serchBtn"> 삭제 </button>
        </div>
        </form>


</>)
}

/*

        let tstate = document.querySelector('.tstate').value;
        console.log('tstate 뭐엇인가 ' +tstate);
        let tdate = document.querySelector('.tdate').value;
        console.log('tstate 뭐엇인가 ' +tdate)


        let info = {
            tstate : tstate,
            tdate : tdate,
            tno : tno
        }

        */
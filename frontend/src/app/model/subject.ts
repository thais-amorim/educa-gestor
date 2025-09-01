export class Subject {
    code: string;
    name: string;
    instructorName: string;
    workload: number;

    constructor(code: string, name: string, instructorName: string, workload: number) {
        this.code = code;
        this.name = name;
        this.instructorName = instructorName;
        this.workload = workload;
    }
}
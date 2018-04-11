package com.programmer.load;

import com.programmer.instructions.CodeFactory;
import com.programmer.instructions.CodeGenerator;
import com.programmer.instructions.Instruction;
import com.programmer.instructions.JumpControl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;
import java.util.List;

import com.programmer.orders.Order;
import com.programmer.path.ProjectPath;
import com.programmer.tags.TagList;
import com.programmer.tags.TagLoader;

public class Compiler {

	private boolean error = false;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private ArrayList<Order> orderList;

	public Compiler(ArrayList<Order> orderList){
		this.orderList = orderList;
	}

	public CodeList compile(String code) throws IOException{

		Segregation segregation = new Segregation(orderList);

		boolean result = segregation.parseCode(code);

		if(result)
		    return  generateBinaryCode(segregation);
        else
            return null;

	}

	private CodeList generateBinaryCode(Segregation segregation){

        List<Instruction> instructions = segregation.getInstructions();
        Map<String, Integer> labels = segregation.getLabels();

	    TagList tagList = TagList.getTagList();
	    tagList.setTagList(TagLoader.loadTags(ProjectPath.getProjectPath().getPath()));

		CodeList codeList = new CodeList();
        CodeFactory codeFactory = new CodeFactory(labels);

        ArrayList<Integer> codeLine = new ArrayList<>();

        String type;
        int code = 0;

		for(Instruction instruction : instructions){

		    type = null;

		    for(Order o : orderList){
		        if(o.getMnemonic().equals(instruction.getOrder())){
		            type = o.getType();
		            code = Integer.parseInt(o.getCode());
		            break;
                }
            }

            if(type != null) {

		        String operand = instruction.getOperand();
		        operand = tagList.findTag(operand);
                CodeGenerator codeGenerator = codeFactory.codeGenerator(type);
                codeLine = codeGenerator.generateCode(operand,
                        code,
                        instruction.getInstructionLine());
            }
            else {
		        LOGGER.warning("Line "+instruction.getInstructionLine()+": cannot find"+
                "order");
                error = true;
            }

            if(codeLine != null)
				codeList.addCompiledCodeLine(codeLine);
			else
				error = true;
		}

		if(error)
		    return null;

		return codeList;
	}

}

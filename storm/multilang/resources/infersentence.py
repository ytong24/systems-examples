from transformers import GPT2LMHeadModel, GPT2Tokenizer
import storm

tokenizer = GPT2Tokenizer.from_pretrained("gpt2")
model = GPT2LMHeadModel.from_pretrained("gpt2")


class SplitSentenceBolt(storm.BasicBolt):
    def process(self, tup):
        testSentence = str(tup.values[0])
        expectedWords = str(tup.values[1])

        inputs = tokenizer.encode(testSentence, return_tensors="pt")
        outputs = model.generate(inputs, max_length=len(inputs[0])+3, do_sample=True, pad_token_id=tokenizer.eos_token_id)
        createdWordsList = [tokenizer.decode(token) for token in outputs[0][-3:]]
        createdWords = " ".join(createdWordsList)
        storm.emit([expectedWords, createdWords])

SplitSentenceBolt().run()


